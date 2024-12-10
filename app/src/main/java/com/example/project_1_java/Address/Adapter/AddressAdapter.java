package com.example.project_1_java.Address.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_1_java.InterFace.OnSelectAddress;
import com.example.project_1_java.Model.InfoModel;
import com.example.project_1_java.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<InfoModel> info;
    private OnSelectAddress onSelect;
    private int selectedPosition;

    public AddressAdapter(List<InfoModel> info, OnSelectAddress onSelect, int id) {
        this.info = info;
        this.onSelect = onSelect;
        this.selectedPosition = id;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_infor, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        InfoModel model = info.get(position);
        holder.bindData(model, position);
    }

    @Override
    public int getItemCount() {
        return info.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, location, locationPlus, delete;
        RadioButton radioButton;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtnameInfor);
            phone = itemView.findViewById(R.id.txtPhoneInfor);
            location = itemView.findViewById(R.id.txtLocation);
            locationPlus = itemView.findViewById(R.id.txtLocationPLus);
            delete = itemView.findViewById(R.id.txtDeleteInfor);
            radioButton = itemView.findViewById(R.id.rb_address);
        }

        void bindData(InfoModel model, int position) {
            name.setText(model.getName());
            phone.setText(model.getPhone());
            location.setText(model.getLocation());
            locationPlus.setText(model.getLocationPlus());

            delete.setOnClickListener(v -> {
                if (info.size()<=1){
                    return;
                }else {
                    onSelect.onDelete(position);
                    info.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, info.size());
                }
            });

            radioButton.setChecked(position == selectedPosition);
            radioButton.setOnClickListener(v -> {
                onSelect.onRadioCheck(position);
                selectedPosition = position;
                notifyItemChanged(selectedPosition);
                notifyItemChanged(position);
            });
        }
    }
    public void updateData(List<InfoModel> newInforList) {
        this.info.clear();
        this.info.addAll(newInforList);
        notifyDataSetChanged();
    }
}
