package com.husseinsilver.store.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.husseinsilver.store.R;
import com.husseinsilver.store.adapters.ProductAdapter;
import com.husseinsilver.store.databinding.ActivityAccessoriesBinding;
import com.husseinsilver.store.models.CartItem;
import com.husseinsilver.store.models.Product;
import java.util.ArrayList;
import java.util.List;

public class AccessoriesActivity extends AppCompatActivity {

    private ActivityAccessoriesBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ProductAdapter adapter;
    private final List<Product> productList = new ArrayList<>();
    private String currentFilter = "ring";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccessoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("خواتم"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("أساور"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("سلاسل"));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: currentFilter = "ring"; break;
                    case 1: currentFilter = "bracelet"; break;
                    case 2: currentFilter = "chain"; break;
                }
                loadAccessories();
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        adapter = new ProductAdapter(this, productList, this::addToCart);
        binding.rvAccessories.setLayoutManager(new LinearLayoutManager(this));
        binding.rvAccessories.setAdapter(adapter);

        loadAccessories();
    }

    private void loadAccessories() {
        binding.progressBar.setVisibility(View.VISIBLE);
        db.collection("products")
                .whereEqualTo("category", "accessories")
                .whereEqualTo("subcategory", currentFilter)
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
                        loadDefaultAccessories();
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    binding.progressBar.setVisibility(View.GONE);
                    loadDefaultAccessories();
                });
    }

    private void loadDefaultAccessories() {
        productList.clear();
        switch (currentFilter) {
            case "ring":
                productList.add(new Product("r1", "خاتم رجالي فضة", "خاتم رجالي عيار 925", "accessories", "ring", 8.0, 6.24, "male"));
                productList.add(new Product("r2", "خاتم نسائي فضة", "خاتم نسائي عيار 925", "accessories", "ring", 5.0, 3.90, "female"));
                productList.add(new Product("r3", "خاتم خطوبة فضة", "خاتم خطوبة عيار 925", "accessories", "ring", 4.0, 3.12, "female"));
                break;
            case "bracelet":
                productList.add(new Product("br1", "سوار رجالي فضة", "سوار رجالي عيار 925", "accessories", "bracelet", 20.0, 15.6, "male"));
                productList.add(new Product("br2", "سوار نسائي فضة", "سوار نسائي عيار 925", "accessories", "bracelet", 12.0, 9.36, "female"));
                productList.add(new Product("br3", "سوار كوبلر فضة", "سوار مزدوج عيار 925", "accessories", "bracelet", 15.0, 11.7, "unisex"));
                break;
            case "chain":
                productList.add(new Product("c1", "سلسلة رجالي فضة", "سلسلة رجالي عيار 925 - 60سم", "accessories", "chain", 25.0, 19.5, "male"));
                productList.add(new Product("c2", "سلسلة نسائي فضة", "سلسلة نسائي عيار 925 - 45سم", "accessories", "chain", 15.0, 11.7, "female"));
                productList.add(new Product("c3", "سلسلة مزدوجة فضة", "سلسلة مزدوجة عيار 925 - 50سم", "accessories", "chain", 20.0, 15.6, "unisex"));
                break;
        }
        adapter.notifyDataSetChanged();
    }

    private void addToCart(Product product) {
        String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (uid == null) return;

        CartItem cartItem = new CartItem(product.getId(), product.getName(),
                product.getPrice(), 1, product.getWeightGrams(), false, "accessories");

        db.collection("carts").document(uid)
                .collection("items")
                .document(product.getId())
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        CartItem existing = doc.toObject(CartItem.class);
                        int newQty = (existing != null ? existing.getQuantity() : 0) + 1;
                        doc.getReference().update("quantity", newQty)
                                .addOnSuccessListener(v ->
                                        Toast.makeText(this, R.string.item_added, Toast.LENGTH_SHORT).show());
                    } else {
                        cartItem.setId(product.getId());
                        doc.getReference().set(cartItem)
                                .addOnSuccessListener(v ->
                                        Toast.makeText(this, R.string.item_added, Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, R.string.error_loading, Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, R.string.error_loading, Toast.LENGTH_SHORT).show());
    }
}
