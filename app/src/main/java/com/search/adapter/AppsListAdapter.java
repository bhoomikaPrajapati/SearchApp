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
    private List<AppList> tempAppListNew;
    
    private Context context;


    public AppsListAdapter(List<AppList> appLists, Context context) {
        super(appLists);
        this.appLists = appLists;
        this.tempAppListNew=appLists;
        
        
        this.context = context;
    }


    @Override
    public AppsListAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppsListAdapter.DataViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_apps_list, parent, false));
    }

    @Override
    public void onBindViewHolder(AppsListAdapter.DataViewHolder holder, int position) {
        AppList appList = tempAppListNew.get(position);
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
                    mFilterResults.values = appLists;
                    mFilterResults.count = appLists.size();
                } else {

                    if (appLists != null && appLists.size() > 0) {
                        for (final AppList mDataBean : appLists) {
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
                tempAppListNew = (ArrayList <AppList>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return tempAppListNew == null ? 0 : tempAppListNew.size();
    }

}



