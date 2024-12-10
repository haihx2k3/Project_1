package com.example.project_1_java.Card.Adapter;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_1_java.Funcion.FormatVND;
import com.example.project_1_java.InterFace.OnCheck;
import com.example.project_1_java.Model.OrderModel;
import com.example.project_1_java.R;
import com.example.project_1_java.Utils.FirebaseUtil;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private final FirebaseUtil util;
    private final OnCheck onCheck;
    private List<OrderModel> orders;
    private final FormatVND fmt;
    private final SparseBooleanArray checkStates;

    public CardAdapter(List<OrderModel> orders, OnCheck onCheck) {
        this.onCheck = onCheck;
        this.orders = orders;
        this.util = new FirebaseUtil();
        this.fmt = new FormatVND();
        this.checkStates = new SparseBooleanArray();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oder, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        OrderModel model = orders.get(position);
        final int[] count = {model.getCount()};

        holder.title.setText(model.getTitle());
        holder.price.setText(fmt.formatCurrency(String.format("%.3f", model.getTotal())));
        holder.count.setText(String.valueOf(count[0]));
        holder.check.setOnCheckedChangeListener(null);
        holder.check.setChecked(checkStates.get(position, false));

        holder.check.setOnCheckedChangeListener((buttonView, isChecked) -> updateCheckState(position, isChecked));

        holder.itemView.setOnLongClickListener(v -> showOverlay(holder, position, model.getId()));

        holder.plus.setOnClickListener(v -> updateItemCount(holder, model, ++count[0], position,true));

        holder.minus.setOnClickListener(v -> {
            updateItemCount(holder, model, --count[0], position,false);
            if (count[0] < 1) {
                removeItem(position, model.getId());
            }
        });

        Glide.with(holder.image).load(model.getImg()).placeholder(R.drawable.waitting).into(holder.image);
    }

    private void updateCheckState(int position, boolean isChecked) {
        checkStates.put(position, isChecked);
        if (isChecked) onCheck.onCheck(position);
        else onCheck.onUnCheck(position);
    }

    private void updateItemCount(CardViewHolder holder, OrderModel model, int newCount, int position,boolean calculate) {
        holder.count.setText(String.valueOf(newCount));
        float totalPrice = fmt.convertStringToFloat(model.getPrice()) * newCount;
        holder.price.setText(fmt.formatCurrency(String.format("%.3f", totalPrice)));
        util.updateOder(model.getId(), newCount, totalPrice);
        if (calculate){
            onCheck.onCheckPlus(position);
        }else {
            onCheck.onCheckMinus(position);
        }
        holder.check.setChecked(true);
        model.setTotal(totalPrice);
        model.setCount(newCount);
    }

    private boolean showOverlay(CardViewHolder holder, int position, String orderId) {
        holder.overLay.setVisibility(View.VISIBLE);
        holder.overLay.setOnClickListener(v -> holder.overLay.setVisibility(View.GONE));
        holder.delete.setOnClickListener(v -> removeItem(position, orderId));
        return true;
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private void removeItem(int position, String orderId) {
        orders.remove(position);
        checkStates.delete(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, orders.size());
        util.deleteOder(orderId);
        onCheck.updateCheckedCount(getCheckedItemCount());
    }

    private int getCheckedItemCount() {
        int count = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (checkStates.get(i, false)) count++;
        }
        return count;
    }

    public void setAllCheckBoxes(boolean state) {
        for (int i = 0; i < getItemCount(); i++) {
            checkStates.put(i, state);
            if (state) onCheck.onCheck(i);
            else onCheck.onUnCheck(i);
        }
        notifyDataSetChanged();
    }

    public void resetAllCheckBoxes() {
        checkStates.clear();
        notifyDataSetChanged();
    }

    public void setOrderList(List<OrderModel> newOrderList) {
        this.orders = newOrderList;
        notifyDataSetChanged();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView image, plus, minus, delete;
        TextView title, price, count;
        LinearLayout overLay;
        CheckBox check;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgOder);
            title = itemView.findViewById(R.id.txtTittleOder);
            price = itemView.findViewById(R.id.txtPriceOder);
            count = itemView.findViewById(R.id.txtCountOder);
            plus = itemView.findViewById(R.id.imgPlusOder);
            minus = itemView.findViewById(R.id.imgMinusOder);
            overLay = itemView.findViewById(R.id.lnOverlayOder);
            delete = itemView.findViewById(R.id.imgDeleteOder);
            check = itemView.findViewById(R.id.ckOderFragment);
        }
    }
}
