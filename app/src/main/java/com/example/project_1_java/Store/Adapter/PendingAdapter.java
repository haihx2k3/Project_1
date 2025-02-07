package com.example.project_1_java.Store.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_1_java.Funcion.FormatVND;
import com.example.project_1_java.InterFace.OnClickAvailable;
import com.example.project_1_java.InterFace.OnClickPending;
import com.example.project_1_java.Model.OrderClassifyModel;
import com.example.project_1_java.R;

import java.util.List;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.ViewHolder> {
    private final List<OrderClassifyModel> sub;
    private final OnClickPending onClick;

    public PendingAdapter(List<OrderClassifyModel> sub, OnClickPending onClick) {
        this.onClick = onClick;
        this.sub = sub;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pending, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderClassifyModel model = sub.get(position);
        holder.txtTittle.setText(model.getTitle());
        holder.txtPrice.setText(FormatVND.formatCurrency(String.format("%.3f",model.getPrice())));
        holder.txtType.setText(model.getType());
        holder.txtQuantity.setText("x"+model.getCount());
        holder.btnCancel.setOnClickListener(v -> {
            onClick.onCancel(position);
            sub.remove(position);
            notifyDataSetChanged();
        });
        holder.btnConfirm.setOnClickListener(v-> {
            onClick.onConfirm(position);
            sub.remove(position);
        });
        Glide.with(holder.img).load(model.getImg()).placeholder(R.drawable.waitting).into(holder.img);
    }

    @Override
    public int getItemCount() {
        if (sub == null) {
            return 0;
        }
        return sub.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTittle, txtPrice,txtQuantity,txtType;
        private final ImageView img;
        private final Button btnConfirm, btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTittle = itemView.findViewById(R.id.txtTitlePending);
            txtPrice = itemView.findViewById(R.id.txtPricePending);
            txtQuantity = itemView.findViewById(R.id.txtQuantityPending);
            txtType = itemView.findViewById(R.id.txtTypePending);
            img = itemView.findViewById(R.id.imgPending);
            btnConfirm = itemView.findViewById(R.id.btnConfirmPending);
            btnCancel = itemView.findViewById(R.id.btnCancelPending);
        }
    }
}
