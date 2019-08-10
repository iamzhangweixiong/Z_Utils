package com.zhangwx.z_utils.Z_Thread.Async;

import android.os.AsyncTask;
import java.net.URL;

public class MyAsyncTask extends AsyncTask<URL, Integer, Long> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Long doInBackground(URL... urls) {
        publishProgress();

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

    }
}
