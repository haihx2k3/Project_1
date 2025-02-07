package com.example.project_1_java.Store.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_1_java.Funcion.FormatVND;
import com.example.project_1_java.InterFace.OnClickPending;
import com.example.project_1_java.InterFace.OnSelect;
import com.example.project_1_java.Model.OrderClassifyModel;
import com.example.project_1_java.R;

import java.util.List;

public class SendingAdapter extends RecyclerView.Adapter<SendingAdapter.ViewHolder> {
    private final List<OrderClassifyModel> sub;
    private final OnSelect onClick;

    public SendingAdapter(List<OrderClassifyModel> sub, OnSelect onClick) {
        this.onClick = onClick;
        this.sub = sub;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sending, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderClassifyModel model = sub.get(position);
        holder.txtId.setText(model.getIdOrder());
        holder.txtDelivery.setText(model.getDelivery());
        holder.btnPrintf.setOnClickListener(v->onClick.onSelected(position));
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
        private final TextView txtId, txtDelivery;
        private final ImageView img;
        private final Button btnPrintf;
        private EditText edtContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtIdSending);
            txtDelivery = itemView.findViewById(R.id.txtDeliverySending);
            img = itemView.findViewById(R.id.imgSending);
            btnPrintf =  itemView.findViewById(R.id.btnPrintfSending);
        }
    }
}
