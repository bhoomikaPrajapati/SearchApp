package com.search.filedata;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;

import com.search.model.AppListModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class WriteDataToTextFile extends AsyncTask<Void, Void, Void> {

    private static final String TAG = WriteDataToTextFile.class.getSimpleName();
    private Context mContext;
    private String fileName;
    private AppListModel appListModel;


    public WriteDataToTextFile(Context mContext, String fileName, AppListModel appListModel) {
        this.mContext = mContext;
        this.fileName = fileName;
        this.appListModel = appListModel;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        writeToFile();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void writeToFile() {



        try {
            String data = "";
            File path = mContext.getFilesDir();
            File file = new File(path, fileName + ".txt");

            FileOutputStream stream = new FileOutputStream(file);

            data = new Gson().toJson(appListModel);

            stream.write(data.getBytes());

            stream.close();

        } catch (Exception e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }






    }


}
