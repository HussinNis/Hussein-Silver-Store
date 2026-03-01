package com.husseinsilver.store.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.husseinsilver.store.R;
import com.husseinsilver.store.databinding.ActivityCalculatorBinding;
import com.husseinsilver.store.network.ApiService;
import com.husseinsilver.store.network.RetrofitClient;
import com.husseinsilver.store.network.SilverPriceResponse;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalculatorActivity extends AppCompatActivity {

    // Standard troy ounce to gram conversion (1 troy oz = 31.1034768 g)
    private static final double TROY_OUNCE_TO_GRAM = 31.1034768;
    private static final double MARGIN = 1.10; // 10% margin
    private ActivityCalculatorBinding binding;
    private double silverPricePerOunce = 0.0;

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
    }

    private void fetchSilverPrice() {
        ApiService apiService = RetrofitClient.getInstance().getApiService();
        apiService.getSilverPrice("", "USD", "XAG")
                .enqueue(new Callback<SilverPriceResponse>() {
                    @Override
                    public void onResponse(Call<SilverPriceResponse> call, Response<SilverPriceResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            double price = response.body().getSilverPriceUSD();
                            if (price > 0) {
                                silverPricePerOunce = price;
                                binding.tvCurrentPrice.setText(
                                        String.format(Locale.getDefault(), "%.4f $", price));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SilverPriceResponse> call, Throwable t) {
                        binding.tvCurrentPrice.setText(R.string.error_loading);
                    }
                });
    }

    private void calculatePrice() {
        String weightStr = binding.etWeight.getText() != null ? binding.etWeight.getText().toString() : "";
        if (weightStr.isEmpty()) {
            Toast.makeText(this, R.string.weight_grams, Toast.LENGTH_SHORT).show();
            return;
        }

        double weightGrams;
        try {
            weightGrams = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.error_loading), Toast.LENGTH_SHORT).show();
            return;
        }

        if (silverPricePerOunce <= 0) {
            Toast.makeText(this, R.string.error_loading, Toast.LENGTH_SHORT).show();
            return;
        }

        double pricePerGram = silverPricePerOunce / TROY_OUNCE_TO_GRAM;
        double finalPrice = weightGrams * pricePerGram * MARGIN;

        binding.cardResult.setVisibility(View.VISIBLE);
        binding.tvResult.setText(String.format(Locale.getDefault(), "%.2f $", finalPrice));
        binding.tvPriceBreakdown.setText(String.format(Locale.getDefault(),
                getString(R.string.price_breakdown_format),
                weightGrams, pricePerGram));
    }
}
