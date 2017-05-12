package com.zhangwx.z_utils.Z_Permission.bridge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.zhangwx.z_utils.Z_Permission.AppSettingsDialog;
import com.zhangwx.z_utils.Z_Permission.EasyPermissions;
import com.zhangwx.z_utils.Z_Permission.PermissionUtils;

import java.util.List;

/**
 * Created by zhangwx on 2017/4/5.
 *
 * <p> 一个空的Activity，用于权限请求的中转 </p>
 */

public class RequestBridgeActivity extends FragmentActivity implements EasyPermissions.PermissionCallbacks {

    private int mRequestCode;
    private String[] mPermissionGroup;
    private String mRationale;
    public static final String KEY_CHECK_TYPE_CODE = "key_check_type_code";
    public static final String KEY_CHECK_RATIONALE = "key_check_rationale";
    public static final String KEY_CHECK_GROUP = "key_check_group";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mRequestCode = intent.getIntExtra(KEY_CHECK_TYPE_CODE, PermissionUtils.DEFAULT_CODE);
        mPermissionGroup = intent.getStringArrayExtra(KEY_CHECK_GROUP);
        mRationale = intent.getStringExtra(KEY_CHECK_RATIONALE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EasyPermissions.requestPermissions(this,
                PermissionUtils.WITHOUT_ICON,
                "RequestBridgeActivity",
                mRationale,
                mRequestCode,
                mPermissionGroup);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        sendResultBroadcast(true);
        finish();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
        sendResultBroadcast(false);
        finish();
    }

    public static void startSelf(Context context, int type, String[] permissionGroup, String rationale) {
        Intent intent = new Intent(context, RequestBridgeActivity.class);
        intent.putExtra(KEY_CHECK_TYPE_CODE, type);
        intent.putExtra(KEY_CHECK_GROUP, permissionGroup);
        intent.putExtra(KEY_CHECK_RATIONALE, rationale);
        context.startActivity(intent);
    }

    private void sendResultBroadcast(boolean isGranted) {
        Intent intent = new Intent(PermissionRequestBridge.PERMISSION_CHECK_ACTION);
        intent.putExtra(PermissionRequestBridge.KEY_CHECK_RESULT, isGranted);
        sendBroadcast(intent);
    }
}
