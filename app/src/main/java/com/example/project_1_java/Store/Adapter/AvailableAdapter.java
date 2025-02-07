package com.example.project_1_java.Store.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_1_java.InterFace.OnClickAvailable;
import com.example.project_1_java.Model.AvailableModel;
import com.example.project_1_java.R;

import java.util.List;

public class AvailableAdapter extends RecyclerView.Adapter<AvailableAdapter.AvailableViewHolder> {
    private List<AvailableModel> sub;
    private OnClickAvailable onClick;

    public AvailableAdapter(List<AvailableModel> sub, OnClickAvailable onClick) {
        this.onClick = onClick;
        this.sub = sub;
    }

    @NonNull
    @Override
    public AvailableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classify_item, parent, false);
        return new AvailableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvailableViewHolder holder, int position) {
        AvailableModel model = sub.get(position);
        holder.namePr.setText(model.getTitle());
        holder.pricePr.setText(model.getPrice());
        Glide.with(holder.itemView)
                .load(model.getUriList() != null && !model.getUriList().isEmpty() ? model.getUriList().get(0) : null)
                .placeholder(R.drawable.waitting)
                .into(holder.img);
        holder.imgFix.setOnClickListener(v -> onClick.onUpdate(position));
        holder.imgDelete.setOnClickListener(v -> {
            sub.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        if (sub == null) {
            return 0;
        }
        return sub.size();
    }

    public static class AvailableViewHolder extends RecyclerView.ViewHolder {
        private TextView namePr, pricePr;
        private ImageView img;
        private ImageButton imgFix, imgDelete;

        public AvailableViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgItem);
            namePr = itemView.findViewById(R.id.txtNameClassify);
            pricePr = itemView.findViewById(R.id.txtPriceClassify);
            imgFix = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
