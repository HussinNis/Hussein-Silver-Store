package com.husseinsilver.store.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.husseinsilver.store.R;
import com.husseinsilver.store.databinding.ActivityCalculatorBinding;
import com.husseinsilver.store.models.CartItem;
import com.husseinsilver.store.network.ApiService;
import com.husseinsilver.store.network.RetrofitClient;
import com.husseinsilver.store.network.SilverPriceResponse;
import com.husseinsilver.store.utils.SharedPreferencesManager;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalculatorActivity extends AppCompatActivity {

    private static final double TROY_OUNCE_TO_GRAM = 31.1034768;
    private static final double MARGIN_RATE = 0.10; // 10%

    private ActivityCalculatorBinding binding;
    private double ouncePriceIls = 0.0;
    private double lastWeightGrams = 0.0;
    private double lastFinalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        fetchSilverPrice();

        binding.btnCalculate.setOnClickListener(v -> calculatePrice());
        binding.btnAddWeightedToCart.setOnClickListener(v -> addWeightedToCart());
    }

    private void fetchSilverPrice() {
        // Try cached price first
        double cached = SharedPreferencesManager.getInstance(this).getSilverPriceILS();
        if (cached > 0) {
            ouncePriceIls = cached;
            binding.tvCurrentPrice.setText(
                    String.format(Locale.getDefault(), "%.2f ₪", ouncePriceIls));
        }

        ApiService apiService = RetrofitClient.getInstance().getApiService();
        apiService.getSilverPrice("", "USD", "XAG,ILS")
                .enqueue(new Callback<SilverPriceResponse>() {
                    @Override
                    public void onResponse(Call<SilverPriceResponse> call,
                                           Response<SilverPriceResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            double price = response.body().getSilverPriceILS();
                            if (price > 0) {
                                ouncePriceIls = price;
                                SharedPreferencesManager.getInstance(CalculatorActivity.this)
                                        .saveSilverPriceILS(price);
                                binding.tvCurrentPrice.setText(
                                        String.format(Locale.getDefault(), "%.2f ₪", price));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SilverPriceResponse> call, Throwable t) {
                        if (ouncePriceIls <= 0) {
                            binding.tvCurrentPrice.setText(R.string.error_loading);
                        }
                    }
                });
    }

    private void calculatePrice() {
        String weightStr = binding.etWeight.getText() != null
                ? binding.etWeight.getText().toString() : "";
        if (weightStr.isEmpty()) {
            Toast.makeText(this, R.string.weight_grams, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            lastWeightGrams = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_loading, Toast.LENGTH_SHORT).show();
            return;
        }

        if (ouncePriceIls <= 0) {
            Toast.makeText(this, R.string.error_no_price, Toast.LENGTH_SHORT).show();
            return;
        }

        double gramPriceIls = ouncePriceIls / TROY_OUNCE_TO_GRAM;
        double base = lastWeightGrams * gramPriceIls;
        double margin = base * MARGIN_RATE;
        lastFinalPrice = base + margin; // base * 1.10

        binding.cardResult.setVisibility(View.VISIBLE);
        binding.tvBasePrice.setText(String.format(Locale.getDefault(), "%.2f ₪", base));
        binding.tvMargin.setText(String.format(Locale.getDefault(), "%.2f ₪", margin));
        binding.tvResult.setText(String.format(Locale.getDefault(), "%.2f ₪", lastFinalPrice));
    }

    private void addWeightedToCart() {
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        if (uid == null) return;
        if (lastWeightGrams <= 0 || lastFinalPrice <= 0) return;

        CartItem cartItem = new CartItem(
                "weighted_item",
                String.format(Locale.getDefault(), "فضة مرجحة %.1f جرام", lastWeightGrams),
                lastFinalPrice, 1, lastWeightGrams, true, "bullion");

        // Use auto-generated document ID so each calculator entry is separate
        FirebaseFirestore.getInstance()
                .collection("carts").document(uid)
                .collection("items")
                .add(cartItem)
                .addOnSuccessListener(ref ->
                        Toast.makeText(this, R.string.item_added, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, R.string.error_loading, Toast.LENGTH_SHORT).show());
    }
}
