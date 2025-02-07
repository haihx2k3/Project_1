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
import com.example.project_1_java.InterFace.OnClick;
import com.example.project_1_java.InterFace.OnClickAvailable;
import com.example.project_1_java.Model.ClassifyModel;
import com.example.project_1_java.R;

import java.util.List;

public class AvailablePrAdapter extends RecyclerView.Adapter<AvailablePrAdapter.SubViewHolder> {
    private List<ClassifyModel> sub;
    private OnClickAvailable onClick;
    public AvailablePrAdapter(List<ClassifyModel> sub , OnClickAvailable onClick){
        this.sub = sub;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.classify_item, parent, false);
        return new SubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubViewHolder holder, int position) {
        ClassifyModel model = sub.get(position);
        holder.namePr.setText(model.getTitle());
        holder.pricePr.setText(model.getPrice());
        Glide.with(holder.itemView)
                .load(model.getImg())
                .placeholder(R.drawable.waitting)
                .into(holder.img);
        holder.imgFix.setVisibility(View.GONE);
        holder.imgDelete.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return sub.size();
    }

    public static class SubViewHolder extends RecyclerView.ViewHolder {
        private TextView namePr, pricePr;
        private ImageView img;
        private ImageButton imgFix, imgDelete;

        public SubViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgItem);
            namePr = itemView.findViewById(R.id.txtNameClassify);
            pricePr = itemView.findViewById(R.id.txtPriceClassify);
            imgFix = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
    public void updateData(List<ClassifyModel> newList) {
        this.sub.clear();
        this.sub.addAll(newList);
        notifyDataSetChanged();
    }
}
