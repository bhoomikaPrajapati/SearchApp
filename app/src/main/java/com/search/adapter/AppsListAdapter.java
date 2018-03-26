package com.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.search.R;
import com.search.customview.BaseRecyclerAdapter;
import com.search.model.AppList;


import java.util.ArrayList;
import java.util.List;


public class AppsListAdapter extends BaseRecyclerAdapter<AppsListAdapter.DataViewHolder, AppList> {

    private List<AppList> appLists;

    private Context context;


    public AppsListAdapter(List<AppList> appLists, Context context) {
        super(appLists);
        this.appLists=appLists;
        this.context = context;
    }


    @Override
    public AppsListAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppsListAdapter.DataViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_apps_list, parent, false));
    }

    @Override
    public void onBindViewHolder(AppsListAdapter.DataViewHolder holder, int position) {
        AppList appList = appLists.get(position);
        holder.tvAppName.setText(appList.getApp_name());
        holder.ivAppIcon.setImageDrawable(appList.getApp_icon());

    }


    class DataViewHolder extends BaseRecyclerAdapter.ViewHolder {

        TextView tvAppName;
        ImageView ivAppIcon;


        public DataViewHolder(View itemView) {
            super(itemView);
            clickableViews(itemView);
            ivAppIcon=(ImageView) itemView.findViewById(R.id.ivAppIcon);
            tvAppName=(TextView) itemView.findViewById(R.id.tvAppName);
        }
    }


    @Override
    public int getItemCount() {
        return  appLists.size();
    }

}



