package com.zhangwx.z_utils.Z_retrofit.Range;

import com.zhangwx.z_utils.Z_retrofit.DownLoadProgressListener;

import java.io.IOException;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Timeout;

public class RangeResponseBody extends ResponseBody {

    private Response mOriginalResponse;
    private DownLoadProgressListener mDownLoadProgressListener;

    private long mOldPoint;

    public RangeResponseBody(Response originalResponse, long startPoint, DownLoadProgressListener downLoadProgressListener) {
        this.mOriginalResponse = originalResponse;
        this.mOldPoint = startPoint;
        this.mDownLoadProgressListener = downLoadProgressListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mOriginalResponse.body().contentType();
    }

    @Override
    public long contentLength() {
        return mOriginalResponse.body().contentLength();
    }

    @Override
    public BufferedSource source() {
        return Okio.buffer(new ForwardingSource(mOriginalResponse.body().source()) {

            private long bytesRead = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long read = super.read(sink, byteCount);
                bytesRead += read == -1 ? 0 : read;
                if (mDownLoadProgressListener != null) {
                    mDownLoadProgressListener.update(
                            bytesRead + mOldPoint,
                            mOriginalResponse.body().contentLength(),
                            bytesRead == -1);
                }
                return super.read(sink, byteCount);
            }

            @Override
            public Timeout timeout() {
                return super.timeout();
            }

            @Override
            public void close() throws IOException {
                super.close();
            }
        });
    }
}
