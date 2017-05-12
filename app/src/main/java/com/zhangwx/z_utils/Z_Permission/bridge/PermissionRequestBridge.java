package com.zhangwx.z_utils.Z_Permission.bridge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.lang.ref.WeakReference;

/**
 * Created by zhangwx on 2017/4/5.
 */

public class PermissionRequestBridge implements IPermissionRequest {

    public static final String PERMISSION_CHECK_ACTION = "permission_check_action";
    public static final String KEY_CHECK_RESULT = "key_check_result";

    private WeakReference<Context> mContext;
    private RequestCallBack mRequestCallBack;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (PERMISSION_CHECK_ACTION.equals(intent.getAction())) {
                boolean isGranted = intent.getBooleanExtra(KEY_CHECK_RESULT, false);
                mRequestCallBack.onRequestResult(isGranted);
            }
        }
    };

    public PermissionRequestBridge(Context context) {
        this.mContext = new WeakReference<>(context);
        registerBroadcast();
    }

    @Override
    public void request(int requestCode, String[] permissionGroup, String rationale, RequestCallBack callBack) {
        mRequestCallBack = callBack;
        RequestBridgeActivity.startSelf(mContext.get(), requestCode, permissionGroup, rationale);
    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PERMISSION_CHECK_ACTION);
        mContext.get().registerReceiver(mReceiver, filter);
    }


    public interface RequestCallBack {
        void onRequestResult(boolean isGranted);
    }
}
