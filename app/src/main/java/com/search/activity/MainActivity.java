package com.search.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.search.R;
import com.search.adapter.AppsListAdapter;
import com.search.customview.BaseRecyclerAdapter;
import com.search.model.AppList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvNotFoundApp;
    private TextView tvSearchMoreApp;
    private RecyclerView rvAllApp;
    private EditText etSearch;
    private ImageView ivClose;
    private AppsListAdapter mAppsListAdapter;
    private List<AppList> mAppListArrayList = new ArrayList<>();
    private List<AppList> mAppTempListArrayList = new ArrayList<>();

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
        etSearch = (EditText) findViewById(R.id.etSearch);
        ivClose=(ImageView) findViewById(R.id.ivClose);


        rvAllApp.setLayoutManager(new GridLayoutManager(this, 4));

        mAppsListAdapter = (AppsListAdapter) new AppsListAdapter(mAppListArrayList, this).setRecycleOnItemClickListener(
                mRecycleOnItemClickListener);
        rvAllApp.setAdapter(mAppsListAdapter);
        setAppData();
        setListener();


    }

    private void setListener() {


        tvSearchMoreApp.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAppListArrayList.clear();

                if (s.length() == 0) {
                    mAppListArrayList.addAll(mAppTempListArrayList);
                    ivClose.setVisibility(View.GONE);
                } else {
                    mAppListArrayList.addAll(filter(mAppTempListArrayList, s.toString()));
                    ivClose.setVisibility(View.VISIBLE);
                }

                mAppsListAdapter.notifyDataSetChanged();
                rvAllApp.scrollToPosition(0);

            }
        });
    }


    private BaseRecyclerAdapter.RecycleOnItemClickListener mRecycleOnItemClickListener = new BaseRecyclerAdapter.RecycleOnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            dismissKeyboard(MainActivity.this);
            Log.e("playstore",""+mAppListArrayList.get(position).getApp_package_name());

            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(mAppListArrayList.get(position).getApp_package_name());
            startActivity(LaunchIntent);
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

             /*   boolean same = false;
                for (int i = 0; i < mAppListArrayList.size(); i++) {
                    if (package_name.equals(mAppListArrayList.get(i).getApp_name()))
                        same = true;
                }
                if (!same) {*/
                    AppList appList = new AppList();
                    appList.setApp_name(app_name);
                    appList.setApp_icon(app_drawable);
                    appList.setApp_package_name(package_name);
                    mAppListArrayList.add(appList);
                    mAppTempListArrayList.add(appList);
                    mAppsListAdapter.notifyDataSetChanged();


            //    }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }

    private ArrayList<AppList> filter(List<AppList> models, String query) {

        ArrayList<AppList> tempArrayList = new ArrayList<>();
        query = query.toLowerCase();


        for (AppList model : models) {
            if (model != null) {
                String text = model.getApp_name().toLowerCase();
                if (text.contains(query)) {
                    tempArrayList.add(model);
                }
            }
        }

        if (tempArrayList.size() == 0) {
            tvNotFoundApp.setVisibility(View.VISIBLE);
            tvNotFoundApp.setText("No Apps found matching " + "\"" + query + "\"");

        } else {
            tvNotFoundApp.setVisibility(View.GONE);

        }

        return tempArrayList;
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.ivClose)
        {
            etSearch.getText().clear();
        }
        else if(view.getId()==R.id.tvSearchMoreApp)
        {
            Intent goToMarket = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://search?q="+etSearch.getText().toString().trim()));
            startActivity(goToMarket);
        }

    }

    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }


    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = this.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


}
