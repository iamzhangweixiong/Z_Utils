package com.zhangwx.z_utils.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import static com.zhangwx.z_utils.Z_Pub.DebugUtils.TAG_SYNC;

/**
 * Created by zhangwx
 * on 2017/7/2.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        Log.e(TAG_SYNC, "SyncAdapter 2");
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        Log.e(TAG_SYNC, "SyncAdapter 3");
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String authority, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.e(TAG_SYNC, "SyncAdapter onPerformSync");
    }
}
