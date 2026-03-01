package com.husseinsilver.store.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthManager {

    private static AuthManager instance;
    private final FirebaseAuth auth;

    private AuthManager() {
        auth = FirebaseAuth.getInstance();
    }

    public static synchronized AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return auth.getCurrentUser() != null;
    }

    public Task<AuthResult> signIn(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> register(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    public void signOut() {
        auth.signOut();
    }
}
