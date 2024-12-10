package com.example.project_1_java.Product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_1_java.InterFace.OnClick;
import com.example.project_1_java.Model.ClassifyModel;
import com.example.project_1_java.R;

import java.util.List;

public class ProductClassifyAdapter extends RecyclerView.Adapter<ProductClassifyAdapter.SubViewHolder> {
    private List<ClassifyModel> sub;
    private OnClick onClick;
    public  ProductClassifyAdapter (List<ClassifyModel> sub , OnClick onClick){
        this.sub = sub;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_productclassify,parent,false);
        return new SubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubViewHolder holder, int position) {
        ClassifyModel model = sub.get(position);
        holder.title.setText(model.getTitle());
        holder.price.setText(model.getPrice());
        Glide.with(holder.itemView.getContext()).load(model.getImg()).placeholder(R.drawable.waitting).into(holder.imageView);
        holder.itemView.setOnClickListener(v->{
            onClick.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return sub.size();
    }

    public static class SubViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title,price;

        public SubViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgSubProduct);
            title = itemView.findViewById(R.id.txtSubTitlePr);
            price = itemView.findViewById(R.id.txtSubPricePr);
        }
    }
}
