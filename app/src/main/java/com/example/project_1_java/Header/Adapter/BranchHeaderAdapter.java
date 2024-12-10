package com.example.project_1_java.Header.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_1_java.InterFace.OnClick;
import com.example.project_1_java.Model.BranchModel;
import com.example.project_1_java.R;

import java.util.List;

public class BranchHeaderAdapter extends  RecyclerView.Adapter<BranchHeaderAdapter.BranchViewHolder>{
    private List<BranchModel> mBranch;
    private OnClick onClick;

    public BranchHeaderAdapter(List<BranchModel> mBranch, OnClick onClick) {
        this.mBranch = mBranch;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_home_layout,parent,false);
        return new BranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {
        BranchModel model = mBranch.get(position);
        holder.txtBranch.setText(model.getTitle());
        holder.imgBranch.setImageResource(model.getImage());
        holder.itemView.setOnClickListener(v->{
            int adapterPosition = holder.getBindingAdapterPosition();
            if (adapterPosition!=RecyclerView.NO_POSITION){
                onClick.onClick(adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
      if (mBranch==null){
          return 0;
      }
      return mBranch.size();
    }

    public static class BranchViewHolder extends RecyclerView.ViewHolder{
        private TextView txtBranch;
        private ImageView imgBranch;
        public BranchViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBranch  = itemView.findViewById(R.id.txtBranchHd);
            imgBranch = itemView.findViewById(R.id.imgBranchHd);

        }
    }
}
