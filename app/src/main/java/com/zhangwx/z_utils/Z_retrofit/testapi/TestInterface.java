package com.zhangwx.z_utils.Z_retrofit.testapi;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface TestInterface {

    @Streaming
    @GET
    @Headers({"api-version:2", "user-agent:HIBPBot-Telegram-by-@stuntguy3000"})
    Observable<ResponseBody> testapi(@Url String url);
}
