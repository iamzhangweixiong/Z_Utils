package com.zhangwx.z_utils.Z_retrofit.Range;

import com.zhangwx.z_utils.Z_retrofit.DownLoadProgressListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class DownLoadRangeInterceptor implements Interceptor {
    private long mStartPoint = 0;
    private DownLoadProgressListener mDownLoadProgressListener;

    public DownLoadRangeInterceptor(long startPoint, DownLoadProgressListener downLoadProgressListener) {
        this.mStartPoint = startPoint;
        this.mDownLoadProgressListener = downLoadProgressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Response response = chain.proceed(chain.request());
        return response.newBuilder()
                .body(new RangeResponseBody(response, mStartPoint, mDownLoadProgressListener))
                .build();
    }
}
