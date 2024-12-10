package com.example.project_1_java.Card.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_1_java.Funcion.FormatVND;
import com.example.project_1_java.InterFace.OnSelect;
import com.example.project_1_java.Model.OrderClassifyModel;
import com.example.project_1_java.R;

import java.util.ArrayList;
import java.util.Calendar;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private ArrayList<OrderClassifyModel> order;
    private OnSelect onChoice;

    public OrderAdapter(ArrayList<OrderClassifyModel> order,OnSelect onChoice){
        this.order = order;
        this.onChoice = onChoice;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oderinformation,parent,false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderClassifyModel model = order.get(position);
        holder.title.setText(model.getTitle());
        holder.type.setText(model.getType());
        holder.price.setText(FormatVND.formatCurrency( String.format("%.3f",model.getPrice())));
        holder.count.setText("x" + model.getCount());
        holder.total.setText(FormatVND.formatCurrency(model.getTotal()));

        holder.delivery.setOnClickListener(v->onChoice.onSelected(position));

        updateDeliveryDay(holder);

        if (model.getDelivery() == 18.300F) {
            holder.deliveryFast.setVisibility(View.VISIBLE);
            holder.deliveryLow.setVisibility(View.GONE);
        } else {
            holder.deliveryFast.setVisibility(View.GONE);
            holder.deliveryLow.setVisibility(View.VISIBLE);
        }

        Glide.with(holder.img.getContext()).load(model.getImg()).placeholder(R.drawable.waitting).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return order.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        TextView title, type, count, price, total, dateFast, dateLow;
        ImageView img;
        LinearLayout deliveryFast, deliveryLow, delivery;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitleOderInf);
            type = itemView.findViewById(R.id.txtClassifyOderInf);
            count = itemView.findViewById(R.id.txtCountOderInf);
            price = itemView.findViewById(R.id.txtPriceOderInf);
            img = itemView.findViewById(R.id.imgProduct);
            deliveryFast = itemView.findViewById(R.id.lnDeliveryFast);
            deliveryLow = itemView.findViewById(R.id.lnDeliveryLow);
            delivery = itemView.findViewById(R.id.lnDelivery);
            total = itemView.findViewById(R.id.txtTotal);
            dateFast = itemView.findViewById(R.id.txtDeliveryInforFast);
            dateLow = itemView.findViewById(R.id.txtDeliveryInforLow);
        }
    }
    private void updateDeliveryDay(OrderViewHolder holder) {
        String fastDeliveryEnd = calculateDateAfterDays(3);
        String slowDeliveryEnd = calculateDateAfterDays(7);
        holder.dateFast.setText("Đảm bảo nhận hàng vào " + fastDeliveryEnd);
        holder.dateLow.setText("Đảm bảo nhận hàng vào " + slowDeliveryEnd);
    }

    private String calculateDateAfterDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        return day + " Tháng " + month;
    }
}
