package com.example.mobileapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileapp.R;
import com.example.mobileapp.model.Branch;

import java.util.List;

public class BranchAdapter extends BaseAdapter {
    private Context context;
    private List<Branch> branchList;

    public BranchAdapter(Context context, List<Branch> brachList) {
        this.context = context;
        this.branchList = brachList;
    }

    @Override
    public int getCount() {
        return branchList != null ? branchList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(context)
                .inflate(R.layout.spinner_item_branch, viewGroup, false);

        TextView txtName = rootView.findViewById(R.id.tv_branch_name);
        ImageView image = rootView.findViewById(R.id.ivbranch);

        txtName.setText(branchList.get(i).getName());
        image.setImageResource(branchList.get(i).getImg());

        return rootView;
    }
}