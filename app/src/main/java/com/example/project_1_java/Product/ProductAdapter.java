package com.example.project_1_java.Product;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.project_1_java.InterFace.OnClick;
import com.example.project_1_java.Model.ModelProduct;
import com.example.project_1_java.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private List<ModelProduct> product;
    private OnClick onClick;

    public ProductAdapter(List<ModelProduct> product, OnClick onClick) {
        this.product = product;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_container, parent, false);
        return new ProductHolder(view);
    }

    @Override
    @SuppressLint("RecyclerView")
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        ModelProduct model = product.get(position);
        holder.title.setText(model.getTitle());
        holder.price.setText(model.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onClick(position);
            }
        });
        Glide.with(holder.imageView)
                .load(model.getUriList() != null && !model.getUriList().isEmpty() ? model.getUriList().get(0) : null)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.loading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.loading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    public static class ProductHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, price;
        View loading;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_propose);
            title = itemView.findViewById(R.id.txtTitleProduct);
            price = itemView.findViewById(R.id.txtPriceProduct);
            loading = itemView.findViewById(R.id.incLoading);
        }
    }
    public void addItems(List<ModelProduct> newItems) {
        int previousSize = product.size();
        product.addAll(newItems);
        notifyItemRangeInserted(previousSize, newItems.size());
    }
}
