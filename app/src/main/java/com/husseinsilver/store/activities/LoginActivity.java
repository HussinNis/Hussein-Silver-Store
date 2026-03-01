package com.husseinsilver.store.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.husseinsilver.store.R;
import com.husseinsilver.store.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";

        if (email.isEmpty()) {
            Toast.makeText(this, R.string.error_email_required, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, R.string.error_password_required, Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnLogin.setEnabled(false);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.btnLogin.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        String errorMsg = task.getException() != null
                                ? task.getException().getMessage()
                                : getString(R.string.error_loading);
                        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
