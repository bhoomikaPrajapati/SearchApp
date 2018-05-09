package com.search.filedata;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;

import com.search.model.AppListModel;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;


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
        BufferedWriter wr = null;
        try {
            // Open the file for writing, without removing its current content.
            wr = new BufferedWriter(new FileWriter(new File(fileName), true));
          String  data = new Gson().toJson(appListModel);
            // Write a sample string to the end of the file.
            wr.write(data);

            wr.close();
        }
        catch(IOException ex) {
            System.err.println("An IOException was caught!");
            ex.printStackTrace();
        }
      /*  finally {
            // Close the file.
            try {

            }
            catch (IOException ex) {
                System.err.println("An IOException was caught!");
                ex.printStackTrace();
            }
        }*/
    }


     /*   try {
            String data = "";



            File dir = mContext.getFilesDir();

            if (!dir.exists()) {
                dir.mkdirs();
            }
            dir.getAbsolutePath();


            File file = new File(dir, fileName + ".txt");

            FileOutputStream stream = new FileOutputStream(file);

            data = new Gson().toJson(appListModel);

            stream.write(data.getBytes());

            stream.close();

        } catch (Exception e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }

*/







}
