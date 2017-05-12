package com.zhangwx.z_utils.Z_Permission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by zhangwx on 2017/3/20.
 */

public class PermissionUtils {

    public static final int ANDROID_M = 23;
    public static final int WITHOUT_ICON = -1;

    public static final int DEFAULT_CODE = Integer.MAX_VALUE;

    public static final int REQUEST_CONTACTS_CODE = 1;
    public static final int REQUEST_PHONE_CODE = 2;
    public static final int REQUEST_CAMERA_CODE = 3;
    public static final int REQUEST_LOCATION_CODE = 4;
    public static final int REQUEST_MICROPHONE_CODE = 5;
    public static final int REQUEST_SMS_CODE = 6;
    public static final int REQUEST_STROAGE_CODE = 7;

    /** 存储读写权限 */
    public static String[] PERMISSIONS_STORAGE_GROUP = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /** 读写联系人权限 */
    public static String[] PERMISSIONS_CONTACTS_GROUP = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS};

    /** 电话权限 */
    public static String[] PERMISSIONS_PHONE_GROUP = {
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.PROCESS_OUTGOING_CALLS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE};

    /** 相机权限 */
    public static String[] PERMISSION_CAMERA_GROUP = {
            Manifest.permission.CAMERA};

    /** 位置权限 */
    public static String[] PERMISSIONS_LOCATION_GROUP = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    /** 麦克风权限 */
    public static String[] PERMISSION_MICROPHONE_GROUP = {
            Manifest.permission.RECORD_AUDIO};

    /** 读取短信权限 */
    public static String[] PERMISSION_SMS_GROUP = {
            Manifest.permission.READ_SMS};

    // 有些地方用 EasyPermission 检查会标红
    /** 检查定位权限 */
    public static boolean checkLocationPermission(Context context) {
        if (ActivityCompat.checkSelfPermission(context, PERMISSIONS_LOCATION_GROUP[0]) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, PERMISSIONS_LOCATION_GROUP[1]) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
}
