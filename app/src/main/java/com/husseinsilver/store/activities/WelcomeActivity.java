package com.husseinsilver.store.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.husseinsilver.store.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {

    private ActivityWelcomeBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToMain();
            return;
        }

        binding.btnLogin.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));

        binding.btnRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
