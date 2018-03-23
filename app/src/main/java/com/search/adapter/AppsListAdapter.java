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

    private List<AppList> appListList;
    private List<AppList> tempAppList;
    private Context context;


    public AppsListAdapter(List<AppList> appListList, Context context) {
        super(appListList);
        this.appListList = appListList;
        this.tempAppList=appListList;
        this.context = context;
    }


    @Override
    public AppsListAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppsListAdapter.DataViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_apps_list, parent, false));
    }

    @Override
    public void onBindViewHolder(AppsListAdapter.DataViewHolder holder, int position) {
        AppList appList = appListList.get(position);
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
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults mFilterResults = new FilterResults();


                List<AppList> mSearchedList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    mFilterResults.values = tempAppList;
                    mFilterResults.count = tempAppList.size();
                } else {

                    if (tempAppList != null && tempAppList.size() > 0) {
                        for (final AppList mDataBean : tempAppList) {
                            if (mDataBean.getApp_name().toLowerCase().contains(charSequence.toString()) || mDataBean.getApp_name().toLowerCase().contains(charSequence.toString()))
                                mSearchedList.add(mDataBean);
                        }
                    }
                    mFilterResults.values = mSearchedList;
                    mFilterResults.count = mSearchedList.size();
                 


                }
                return mFilterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence,
                                          FilterResults results) {
                appListList = (ArrayList <AppList>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return appListList == null ? 0 : appListList.size();
    }

}



