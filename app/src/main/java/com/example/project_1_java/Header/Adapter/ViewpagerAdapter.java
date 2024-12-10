package com.example.project_1_java.Header.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.project_1_java.R;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerAdapter extends RecyclerView.Adapter<ViewpagerAdapter.ViewpagerHolder> {
    private List<String> image;
    private ViewPager2 viewPager;

    public ViewpagerAdapter(ArrayList<String> image, ViewPager2 viewPager) {
        this.image = image;
        this.viewPager = viewPager;
    }

    public static class ViewpagerHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewpagerHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgVp);
        }
    }

    @NonNull
    @Override
    public ViewpagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_container, parent, false);
        return new ViewpagerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewpagerHolder holder, int position) {
        Glide.with(holder.imageView.getContext())
                .load(image.get(position))
                .placeholder(R.drawable.waitting)
                .into(holder.imageView);
        if (position == image.size()-1){
            viewPager.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return image.size();
    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            image.addAll(image);
            notifyDataSetChanged();
        }
    };
}
