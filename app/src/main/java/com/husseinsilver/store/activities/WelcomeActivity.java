package com.husseinsilver.store.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.husseinsilver.store.R;
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

        binding.btnLogin.setOnClickListener(v -> showAuthDialog());
    }

    private void showAuthDialog() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_auth_question)
                .setPositiveButton(R.string.has_account, (dialog, which) ->
                        startActivity(new Intent(this, LoginActivity.class)))
                .setNegativeButton(R.string.create_account, (dialog, which) ->
                        startActivity(new Intent(this, RegisterActivity.class)))
                .show();
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
