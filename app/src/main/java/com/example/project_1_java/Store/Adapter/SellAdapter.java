package com.example.project_1_java.Store.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_1_java.R;

import java.util.List;

public class SellAdapter extends RecyclerView.Adapter<SellAdapter.SellViewHolder> {
    private List<Uri> imageUris;
    public SellAdapter(List<Uri> imageUris){
        this.imageUris = imageUris;
    }

    @NonNull
    @Override
    public SellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_item,parent,false);
        return new SellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellViewHolder holder, int position) {
        Uri uri = imageUris.get(position);
        holder.imageView.setImageURI(uri);
        holder.delete.setOnClickListener(v->{
            imageUris.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,imageUris.size());
        });
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    public static class SellViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        Button delete;
        public SellViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgSell);
            delete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
