package com.husseinsilver.store.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.husseinsilver.store.R;
import com.husseinsilver.store.databinding.ActivityMainBinding;
import com.husseinsilver.store.network.ApiService;
import com.husseinsilver.store.network.RetrofitClient;
import com.husseinsilver.store.network.SilverPriceResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private Handler handler;
    private Runnable priceUpdateRunnable;
    private static final long UPDATE_INTERVAL = 60000; // 60 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        handler = new Handler(Looper.getMainLooper());

        setSupportActionBar(binding.toolbar);

        binding.cardBullion.setOnClickListener(v ->
                startActivity(new Intent(this, BullionActivity.class)));
        binding.cardAccessories.setOnClickListener(v ->
                startActivity(new Intent(this, AccessoriesActivity.class)));
        binding.cardCalculator.setOnClickListener(v ->
                startActivity(new Intent(this, CalculatorActivity.class)));
        binding.cardCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        priceUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                fetchSilverPrice();
                handler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
        handler.post(priceUpdateRunnable);
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
                                binding.tvSilverPrice.setText(String.format(Locale.getDefault(), "%.4f $", price));
                                String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                binding.tvLastUpdated.setText(getString(R.string.updated) + ": " + time);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SilverPriceResponse> call, Throwable t) {
                        binding.tvSilverPrice.setText(R.string.error_loading);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        menu.add(0, 1, 0, R.string.logout);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == 1) {
            mAuth.signOut();
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && priceUpdateRunnable != null) {
            handler.removeCallbacks(priceUpdateRunnable);
        }
    }
}
