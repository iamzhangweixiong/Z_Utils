package com.zhangwx.z_utils.Z_retrofit.loadfile;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApkloadInterface {

    @Streaming
    @GET
    Observable<ResponseBody> downloadApk(@Url String url);
}
