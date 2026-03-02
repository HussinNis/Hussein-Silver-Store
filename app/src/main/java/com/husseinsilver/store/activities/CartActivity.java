package com.husseinsilver.store.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.husseinsilver.store.R;
import com.husseinsilver.store.adapters.CartAdapter;
import com.husseinsilver.store.databinding.ActivityCartBinding;
import com.husseinsilver.store.models.CartItem;
import com.husseinsilver.store.utils.Constants;
import com.husseinsilver.store.utils.SharedPreferencesManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private CartAdapter adapter;
    private final List<CartItem> cartItems = new ArrayList<>();
    private double ouncePriceIls = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        ouncePriceIls = SharedPreferencesManager.getInstance(this).getSilverPriceILS();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new CartAdapter(this, cartItems, new CartAdapter.OnCartItemListener() {
            @Override
            public void onRemove(CartItem item, int position) {
                removeCartItem(item, position);
            }

            @Override
            public void onQuantityChanged(CartItem item, int newQuantity) {
                updateCartItemQuantity(item);
                updateTotals();
            }
        });

        binding.rvCart.setLayoutManager(new LinearLayoutManager(this));
        binding.rvCart.setAdapter(adapter);

        binding.btnCheckout.setOnClickListener(v ->
                Toast.makeText(this, R.string.checkout_success, Toast.LENGTH_LONG).show());

        binding.btnClearCart.setOnClickListener(v -> clearCart());

        loadCartItems();
    }

    private void loadCartItems() {
        String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (uid == null) return;

        binding.progressBar.setVisibility(View.VISIBLE);
        db.collection("carts").document(uid)
                .collection("items")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    binding.progressBar.setVisibility(View.GONE);
                    cartItems.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        CartItem item = doc.toObject(CartItem.class);
                        item.setId(doc.getId());
                        cartItems.add(item);
                    }
                    adapter.notifyDataSetChanged();
                    updateTotals();
                    updateEmptyState();
                })
                .addOnFailureListener(e -> {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, R.string.error_loading, Toast.LENGTH_SHORT).show();
                });
    }

    private void removeCartItem(CartItem item, int position) {
        String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (uid == null || item.getId() == null) return;

        db.collection("carts").document(uid)
                .collection("items").document(item.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    cartItems.remove(position);
                    adapter.notifyItemRemoved(position);
                    updateTotals();
                    updateEmptyState();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, R.string.error_loading, Toast.LENGTH_SHORT).show());
    }

    private void updateCartItemQuantity(CartItem item) {
        String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (uid == null || item.getId() == null) return;

        db.collection("carts").document(uid)
                .collection("items").document(item.getId())
                .update("quantity", item.getQuantity());
    }

    private void clearCart() {
        String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (uid == null) return;

        db.collection("carts").document(uid)
                .collection("items")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        doc.getReference().delete();
                    }
                    cartItems.clear();
                    adapter.notifyDataSetChanged();
                    updateTotals();
                    updateEmptyState();
                });
    }

    private void updateTotals() {
        double gramPriceIls = ouncePriceIls > 0
                ? ouncePriceIls / Constants.TROY_OUNCE_TO_GRAM : 0.0;

        double weightedSubtotal = 0;
        double accessoriesSubtotal = 0;

        for (CartItem item : cartItems) {
            if (item.isWeighted()) {
                weightedSubtotal += item.getQuantity() * item.getWeightGrams() * gramPriceIls;
            } else {
                accessoriesSubtotal += item.getQuantity() * item.getPriceIls();
            }
        }

        double margin = weightedSubtotal * 0.10;
        double total = weightedSubtotal + margin + accessoriesSubtotal;

        binding.tvWeightedSubtotal.setText(
                String.format(Locale.getDefault(), "%.2f ₪", weightedSubtotal));
        binding.tvMargin.setText(
                String.format(Locale.getDefault(), "%.2f ₪", margin));
        binding.tvAccessoriesSubtotal.setText(
                String.format(Locale.getDefault(), "%.2f ₪", accessoriesSubtotal));
        binding.tvTotal.setText(
                String.format(Locale.getDefault(), "%.2f ₪", total));

        if (ouncePriceIls <= 0) {
            Toast.makeText(this, R.string.error_no_price, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEmptyState() {
        if (cartItems.isEmpty()) {
            binding.tvEmptyCart.setVisibility(View.VISIBLE);
            binding.rvCart.setVisibility(View.GONE);
            binding.layoutFooter.setVisibility(View.GONE);
        } else {
            binding.tvEmptyCart.setVisibility(View.GONE);
            binding.rvCart.setVisibility(View.VISIBLE);
            binding.layoutFooter.setVisibility(View.VISIBLE);
        }
    }
}
