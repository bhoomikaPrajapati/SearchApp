package com.search.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.search.R;
import com.search.adapter.AppsListAdapter;
import com.search.customview.BaseRecyclerAdapter;
import com.search.model.AppList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvNotFoundApp;
    private TextView tvSearchMoreApp;
    private RecyclerView rvAllApp;
    private EditText etSearch;
    private AppsListAdapter mAppsListAdapter;
    private List<AppList> mAppListArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
    }

    private void initialization() {
        tvNotFoundApp = (TextView) findViewById(R.id.tvNotFoundApp);
        tvSearchMoreApp = (TextView) findViewById(R.id.tvSearchMoreApp);
        rvAllApp = (RecyclerView) findViewById(R.id.rvAppList);
        etSearch=(EditText) findViewById(R.id.etSearch);


        rvAllApp.setLayoutManager(new GridLayoutManager(this, 4));

        mAppsListAdapter = (AppsListAdapter) new AppsListAdapter(mAppListArrayList,this).setRecycleOnItemClickListener(
                mRecycleOnItemClickListener);
        rvAllApp.setAdapter(mAppsListAdapter);
        setAppData();
        setListener();
    }

    private void setListener() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {
                mAppsListAdapter.getFilter().filter(cs);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private BaseRecyclerAdapter.RecycleOnItemClickListener mRecycleOnItemClickListener = new BaseRecyclerAdapter.RecycleOnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(mAppListArrayList.get(position).getApp_package_name());
            startActivity( LaunchIntent );
        }
    };


    private void setAppData() {
        PackageManager pm = getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN, null);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> packages = pm.queryIntentActivities(main, 0);


        for (ResolveInfo resolve_info : packages) {
            try {
                String package_name = resolve_info.activityInfo.packageName;
                String app_name = (String) pm.getApplicationLabel(pm.getApplicationInfo(package_name, PackageManager.GET_META_DATA));
                Drawable app_drawable = resolve_info.activityInfo.loadIcon(this.getPackageManager());

                boolean same = false;
                for (int i = 0; i < mAppListArrayList.size(); i++) {
                    if (package_name.equals(mAppListArrayList.get(i).getApp_name()))
                        same = true;
                }
                if (!same) {
                    AppList appList = new AppList();
                    appList.setApp_name(app_name);
                    appList.setApp_icon(app_drawable);
                    appList.setApp_package_name(package_name);
                    mAppListArrayList.add(appList);
                    mAppsListAdapter.notifyDataSetChanged();


                }
                Log.e("Check", "package = <" + package_name + "> name = <" + app_name + ">" + "> icon = <" + app_drawable + ">");
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }
}
