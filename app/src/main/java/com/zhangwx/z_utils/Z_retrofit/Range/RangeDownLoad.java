package com.zhangwx.z_utils.Z_retrofit.Range;

import android.util.Log;
import com.zhangwx.z_utils.MyApplication;
import com.zhangwx.z_utils.Z_Utils.CloseUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RangeDownLoad {
    public static final String TAG = "RangeDownLoad";
    private static final String cachePath = MyApplication.getContext().getFilesDir() + File.separator;
    private static final String RANGE = "RANGE";
    private long mStartPoint = 0;

    public void downLoad(String url) {
        mStartPoint = getFile().length();
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new DownLoadRangeInterceptor(
                        mStartPoint,
                        (bytesRead, contentLength, done) -> {
                            Log.d(TAG, String.valueOf(bytesRead / contentLength));
                        }))
                .build();

        final Request request = new Request.Builder()
                .url(url)
                .header(RANGE, "bytes=" + mStartPoint + "-")
                .build();
        final Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final long length = request.body().contentLength();

                if (length == 0) {
                    Log.d(TAG, "Load Completed");
                    return;
                }

                int len;
                final byte[] buf = new byte[1024];
                final InputStream is = response.body().byteStream();
                final BufferedInputStream bis = new BufferedInputStream(is);
                RandomAccessFile accessFile = null;
                try {
                    accessFile = new RandomAccessFile(getFile(), "rwd");
                    accessFile.seek(mStartPoint);
                    while ((len = bis.read(buf)) != -1) {
                        accessFile.write(buf, 0, len);
                    }
                    Log.d(TAG, "Load Completed");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    CloseUtil.safeClose(is);
                    CloseUtil.safeClose(bis);
                    CloseUtil.safeClose(accessFile);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    private File getFile() {
        final File file = new File(cachePath + "xxxx.xx");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
