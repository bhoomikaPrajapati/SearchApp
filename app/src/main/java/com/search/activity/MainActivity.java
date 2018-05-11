package com.search.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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

import com.google.gson.Gson;
import com.search.R;
import com.search.adapter.AppsListAdapter;
import com.search.customview.BaseRecyclerAdapter;
import com.search.listener.ApplicationBroadcastService;
import com.search.model.AppList;
import com.search.model.AppListModel;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter("MESSAGE"));

        initialization();

    }

    private void initialization() {

        tvNotFoundApp = findViewById(R.id.tvNotFoundApp);
        tvSearchMoreApp = findViewById(R.id.tvSearchMoreApp);
        rvAllApp = findViewById(R.id.rvAppList);
        etSearch = findViewById(R.id.etSearch);
        ivClose = findViewById(R.id.ivClose);


        rvAllApp.setLayoutManager(new GridLayoutManager(this, 4));

        mAppsListAdapter = (AppsListAdapter) new AppsListAdapter(mAppListArrayList, this).setRecycleOnItemClickListener(
                mRecycleOnItemClickListener);
        rvAllApp.setAdapter(mAppsListAdapter);


        String modelData = readFromFile();
        if (modelData != null && !modelData.equalsIgnoreCase(""))
            setAppListData(new Gson().fromJson(modelData, AppListModel.class));
        else
            setAppData();

        setListener();


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /**
     * Broadcast receiver to update chat screen when user uses application.
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("BroadCast Receiver", "onStart: called");
            setAppData();
        }
    };


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
            Log.e("playstore", "" + mAppListArrayList.get(position).getApp_package_name());
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(mAppListArrayList.get(position).getApp_package_name());
            startActivity(LaunchIntent);
        }
    };


    private void setAppData() {
        mAppListArrayList.clear();
        mAppTempListArrayList.clear();
        clearApplicationData();
        PackageManager pm = getPackageManager();
        Intent main = new Intent(Intent.ACTION_MAIN, null);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> packages = pm.queryIntentActivities(main, 0);

        for (ResolveInfo resolve_info : packages) {
                try {
                    String package_name = resolve_info.activityInfo.packageName;
                    String app_name = (String) pm.getApplicationLabel(pm.getApplicationInfo(package_name, PackageManager.GET_META_DATA));
                   // Drawable app_drawable = resolve_info.activityInfo.loadIcon(this.getPackageManager());
                    Drawable app_drawable = resolve_info.activityInfo.loadIcon(getPackageManager());

                    boolean same = false;
                    for (int i = 0; i < mAppListArrayList.size(); i++) {
                        if (package_name.equals(mAppListArrayList.get(i).getApp_package_name()))
                            same = true;
                    }
                    if(package_name.equalsIgnoreCase("com.search")){
                        same=true;
                    }

                    if (!same) {
                        Bitmap bitmap = ((BitmapDrawable) app_drawable).getBitmap();
                        createIconFile(bitmap, app_name);
                        AppList appList = new AppList();
                        appList.setApp_name(app_name);

                        appList.setApp_package_name(package_name);
                        mAppListArrayList.add(appList);
                        mAppTempListArrayList.add(appList);
                        mAppsListAdapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            AppListModel appListModel = new AppListModel();
            appListModel.setAppListArrayList(mAppListArrayList);
            String data = new Gson().toJson(appListModel);
            writeToFile(data);


        }

    private void createIconFile(Bitmap bitmap, String filename) {
        //create a file to write bitmap data
        File f = new File(getCacheDir(), filename);
        Log.e("filePath", f.getAbsolutePath());
        try {
            f.createNewFile();
            //Convert bitmap to byte array

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
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
        if (view.getId() == R.id.ivClose) {
            etSearch.getText().clear();
        } else if (view.getId() == R.id.tvSearchMoreApp) {
            Intent goToMarket = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://search?q=" + etSearch.getText().toString().trim()));
            startActivity(goToMarket);
        }

    }

    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }


    private void setAppListData(AppListModel appListModel) {
        if (appListModel == null) {
            setAppData();
        } else {
            mAppListArrayList.clear();
            mAppTempListArrayList.clear();
            mAppListArrayList.addAll(appListModel.getAppListArrayList());
            mAppTempListArrayList.addAll(mAppListArrayList);
            mAppsListAdapter.notifyDataSetChanged();

        }

    }

    private void writeToFile(String data) {
        File file = new File("config.txt");
        boolean deleted = file.delete();
        Log.e("file Delete or not", "" + deleted);

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.e("file write", "write");
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("config.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
                Log.e("file read", "read");
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBroadcastReceiver.getDebugUnregister();
    }
}