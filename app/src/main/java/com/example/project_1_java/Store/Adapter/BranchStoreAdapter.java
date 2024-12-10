package com.example.project_1_java.Store.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_1_java.Model.BranchModel;
import com.example.project_1_java.R;

import java.util.List;

public class BranchStoreAdapter extends ArrayAdapter<BranchModel> {
    private final Activity activity;
    private final List<BranchModel> list;
    public BranchStoreAdapter(Activity activity, List<BranchModel> list){
        super(activity, R.layout.layout_branch);
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    private View initView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.layout_branch, parent, false);

        TextView text = rowView.findViewById(R.id.txtBranch);
        ImageView image = rowView.findViewById(R.id.imgBranch);

        text.setText(list.get(position).getTitle());
        image.setImageResource(list.get(position).getImage());

        return rowView;
    }
}
