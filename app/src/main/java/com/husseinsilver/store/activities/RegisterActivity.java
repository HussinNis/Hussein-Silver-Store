package com.husseinsilver.store.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.husseinsilver.store.R;
import com.husseinsilver.store.databinding.ActivityRegisterBinding;
import com.husseinsilver.store.models.User;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.btnRegister.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String fullName = binding.etFullName.getText() != null ? binding.etFullName.getText().toString().trim() : "";
        String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
        String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";
        String confirmPassword = binding.etConfirmPassword.getText() != null ? binding.etConfirmPassword.getText().toString().trim() : "";

        if (fullName.isEmpty()) {
            Toast.makeText(this, R.string.error_name_required, Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(this, R.string.error_email_required, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, R.string.error_password_required, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, R.string.error_password_mismatch, Toast.LENGTH_SHORT).show();
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnRegister.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult().getUser() != null) {
                        String uid = task.getResult().getUser().getUid();
                        User user = new User(uid, fullName, email);
                        db.collection("users").document(uid)
                                .set(user)
                                .addOnCompleteListener(dbTask -> {
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.btnRegister.setEnabled(true);
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(this, R.string.register_success, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(this, R.string.error_loading, Toast.LENGTH_LONG).show();
                                    }
                                });
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.btnRegister.setEnabled(true);
                        String errorMsg = task.getException() != null
                                ? task.getException().getMessage()
                                : getString(R.string.error_loading);
                        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
