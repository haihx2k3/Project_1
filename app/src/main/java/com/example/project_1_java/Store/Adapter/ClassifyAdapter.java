package com.example.project_1_java.Store.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_1_java.InterFace.OnClickClassify;
import com.example.project_1_java.Model.ClassifyModel;
import com.example.project_1_java.R;

import java.util.List;

public class ClassifyAdapter extends RecyclerView.Adapter<ClassifyAdapter.ClassifyViewHolder> {
    private List<ClassifyModel> sub;
    private OnClickClassify onClick;
    public ClassifyAdapter(List<ClassifyModel> sub, OnClickClassify onClick){
        this.sub = sub;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public ClassifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classify_item,parent,false);
        return new ClassifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassifyViewHolder holder, int position) {
        ClassifyModel model = sub.get(position);
        holder.imageView.setOnClickListener(v -> onClick.onSelectImage(position));
        holder.edit.setOnClickListener(v -> onClick.onUpdate(position));
        holder.delete.setOnClickListener(v -> {
            sub.remove(position);
            notifyDataSetChanged();
        });

        Glide.with(holder.itemView.getContext())
                .load(model.getImg())
                .placeholder(R.drawable.baseline_image_search_24)
                .into(holder.imageView);

        holder.title.setText(model.getTitle());
        holder.price.setText(model.getPrice());
    }

    @Override
    public int getItemCount() {
        return sub.size();
    }

    public static class ClassifyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,edit,delete;
        TextView title, price;

        public ClassifyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtNameClassify);
            price = itemView.findViewById(R.id.txtPriceClassify);
            imageView = itemView.findViewById(R.id.imgItem);
            edit = itemView.findViewById(R.id.imgEdit);
            delete = itemView.findViewById(R.id.imgDelete);
        }
    }
    public void updateData(List<ClassifyModel> newData) {
        if (newData != null) {
            this.sub.clear();
            this.sub.addAll(newData);
            notifyDataSetChanged();
        }
    }
}
