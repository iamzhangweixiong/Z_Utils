package com.zhangwx.z_utils.Z_retrofit;

public interface DownLoadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
