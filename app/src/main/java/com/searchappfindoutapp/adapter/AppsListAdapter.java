package com.searchappfindoutapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.searchappfindoutapp.R;
import com.searchappfindoutapp.customview.BaseRecyclerAdapter;
import com.searchappfindoutapp.model.AppList;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


public class AppsListAdapter extends BaseRecyclerAdapter<AppsListAdapter.DataViewHolder, AppList> {

    private List<AppList> appLists;
    private Context context;


    public AppsListAdapter(List<AppList> appLists, Context context) {
        super(appLists);
        this.appLists = appLists;
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
       /* // holder.ivAppIcon.setImageDrawable(appList.getApp_icon());


     //   Bitmap bitmap = BitmapFactory.decodeByteArray(appList.getApp_icon(), 0, appList.getApp_icon().length);

        BitmapFactory.Options options = new BitmapFactory.Options();
      //  options.inPreferredConfig = Bitmap.Config.ARGB_8888;
      //  Bitmap bitmap = BitmapFactory.decodeFile(appList.getApp_name(), options);
        Bitmap bitmap = BitmapFactory.decodeFile(appList.getApp_name());
        holder.ivAppIcon.setImageBitmap(bitmap);
*/

        Bitmap bitmap = null;
        File f = new File(context.getCacheDir()+"/"+appList.getApp_name().replace("/",""));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        holder.ivAppIcon.setImageBitmap(bitmap);
    }


    class DataViewHolder extends BaseRecyclerAdapter.ViewHolder {

        TextView tvAppName;
        ImageView ivAppIcon;


        public DataViewHolder(View itemView) {
            super(itemView);
            clickableViews(itemView);
            ivAppIcon = (ImageView) itemView.findViewById(R.id.ivAppIcon);
            tvAppName = (TextView) itemView.findViewById(R.id.tvAppName);
        }
    }

    @Override
    public int getItemCount() {
        return appLists.size();
    }


}



