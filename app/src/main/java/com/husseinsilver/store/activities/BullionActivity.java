package com.husseinsilver.store.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.husseinsilver.store.R;
import com.husseinsilver.store.adapters.ProductAdapter;
import com.husseinsilver.store.databinding.ActivityBullionBinding;
import com.husseinsilver.store.models.CartItem;
import com.husseinsilver.store.models.Product;
import java.util.ArrayList;
import java.util.List;

public class BullionActivity extends AppCompatActivity {

    private ActivityBullionBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ProductAdapter adapter;
    private final List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBullionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        adapter = new ProductAdapter(this, productList, this::addToCart);
        binding.rvBullion.setLayoutManager(new LinearLayoutManager(this));
        binding.rvBullion.setAdapter(adapter);

        loadBullionProducts();
    }

    private void loadBullionProducts() {
        binding.progressBar.setVisibility(View.VISIBLE);
        db.collection("products")
                .whereEqualTo("category", "bullion")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    binding.progressBar.setVisibility(View.GONE);
                    productList.clear();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Product product = doc.toObject(Product.class);
                        if (product != null) {
                            product.setId(doc.getId());
                            productList.add(product);
                        }
                    }
                    if (productList.isEmpty()) {
                        loadDefaultBullionProducts();
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    binding.progressBar.setVisibility(View.GONE);
                    loadDefaultBullionProducts();
                });
    }

    private void loadDefaultBullionProducts() {
        productList.clear();
        productList.add(new Product("b1", "سبيكة 250 جرام", "فضة خالصة 999", "bullion", "bar", 250, 195.0, "unisex"));
        productList.add(new Product("b2", "سبيكة 500 جرام", "فضة خالصة 999", "bullion", "bar", 500, 390.0, "unisex"));
        productList.add(new Product("b3", "سبيكة 1 كيلو", "فضة خالصة 999", "bullion", "bar", 1000, 780.0, "unisex"));
        productList.add(new Product("b4", "أونصة إيطالية", "فضة إيطالية عيار 925", "bullion", "italian", 31.1, 24.2, "unisex"));
        productList.add(new Product("b5", "أونصة فرنسية", "فضة فرنسية عيار 925", "bullion", "french", 31.1, 24.5, "unisex"));
        productList.add(new Product("b6", "أونصة سويسرية", "فضة سويسرية عيار 999", "bullion", "swiss", 31.1, 25.0, "unisex"));
        adapter.notifyDataSetChanged();
    }

    private void addToCart(Product product) {
        String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (uid == null) return;

        CartItem cartItem = new CartItem(product.getId(), product.getName(),
                product.getPrice(), 1, product.getWeightGrams());

        db.collection("carts").document(uid)
                .collection("items")
                .add(cartItem)
                .addOnSuccessListener(ref -> {
                    cartItem.setId(ref.getId());
                    Toast.makeText(this, R.string.item_added, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, R.string.error_loading, Toast.LENGTH_SHORT).show());
    }
}
