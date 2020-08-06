package com.zhangwx.z_utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

public class JsonTest {

    public static final String GSON_STRING = "{\"quicConfig\":{\n" +
            "        \"enable_quic\":true,\n" +
            "        \"quic_idle_timeout_sec\":30,\n" +
            "        \"preconnect_num_streams\":3,\n" +
            "        \"preconnect_non_altsvc\":true,\n" +
            "        \"altsvc_broken_time_base\":300,\n" +
            "        \"altsvc_broken_time_max\":86400,\n" +
            "        \"enable_x_net\":false\n" +
            "    }}";

    public static void main(String[] args) {
        QuicConfig config = new Gson().fromJson(GSON_STRING, QuicConfig.class);
        System.out.println(config.mEnableQuic);
    }


    public static class QuicConfig {
        @SerializedName("enable_quic")
        public boolean mEnableQuic;

        @SerializedName("quic_idle_timeout_sec")
        public int mQuicIdleTimeoutSec;

        @SerializedName("preconnect_num_streams")
        public int mPreconnectNumStreams;

        @SerializedName("preconnect_non_altsvc")
        public boolean mPreconnectNonAltsvc;

        @SerializedName("altsvc_broken_time_base")
        public int mAltsvcBrokenTimeBase;

        @SerializedName("altsvc_broken_time_max")
        public int mAltsvcBrokenTimeMax;

        @SerializedName("enable_x_net")
        public boolean mEnableXNet;

//        @SerializedName("quic_hints")
//        public JsonArray mQuicHints;
//
//        @SerializedName("preconnect_urls")
//        public JsonArray mPreconnectUrls;
    }
}
