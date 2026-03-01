package com.husseinsilver.store.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.husseinsilver.store.R;
import com.husseinsilver.store.models.CartItem;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartItemListener {
        void onRemove(CartItem item, int position);
        void onQuantityChanged(CartItem item, int newQuantity);
    }

    private final Context context;
    private final List<CartItem> cartItems;
    private final OnCartItemListener listener;

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.tvItemName.setText(item.getProductName());
        holder.tvItemPrice.setText(String.format(Locale.getDefault(), "%.2f $", item.getTotalPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        holder.btnRemove.setOnClickListener(v -> {
            if (listener != null) listener.onRemove(item, position);
        });

        holder.btnIncrease.setOnClickListener(v -> {
            int newQty = item.getQuantity() + 1;
            item.setQuantity(newQty);
            holder.tvQuantity.setText(String.valueOf(newQty));
            holder.tvItemPrice.setText(String.format(Locale.getDefault(), "%.2f $", item.getTotalPrice()));
            if (listener != null) listener.onQuantityChanged(item, newQty);
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                int newQty = item.getQuantity() - 1;
                item.setQuantity(newQty);
                holder.tvQuantity.setText(String.valueOf(newQty));
                holder.tvItemPrice.setText(String.format(Locale.getDefault(), "%.2f $", item.getTotalPrice()));
                if (listener != null) listener.onQuantityChanged(item, newQty);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemPrice, tvQuantity;
        ImageButton btnRemove, btnIncrease, btnDecrease;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemPrice = itemView.findViewById(R.id.tvItemPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }
    }
}
