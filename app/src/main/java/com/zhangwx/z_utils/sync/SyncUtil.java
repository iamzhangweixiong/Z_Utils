package com.zhangwx.z_utils.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.zhangwx.z_utils.MyApplication;
import com.zhangwx.z_utils.R;

import static com.zhangwx.z_utils.Z_Pub.DebugUtils.TAG_SYNC;

/**
 * Created by zhangwx
 * on 2017/7/3.
 *
 * addPeriodicSync 这个方法的 pollFrequency 有问题，
 * 官方文档上写的单位是秒，但照目前测试结果来看根本没有对上，不同手机表现很不一样，同一手机的表现也没有呈现周期性
 * 当我设置为 10 的时候，sumsung 7.0 周期为 10 分钟左右，而原生 n5 6.0 表现为一分钟或者两分钟。。。。
 */

public class SyncUtil {

    public static final String ACCOUNT_NAME = "Z_Utils";
    private static final String ACCOUNT_TYPE = MyApplication.getContext().getResources().getString(R.string.account_type);

    //安装后默认后台同步的时间间隔 in seconds
    public static long SYNC_FREQUENCY = 10;

    public static void createSyncAccount(Context context) {
        // Create account, if it's missing. (Either first run, or user has deleted account.)
        Account account = GetAccount();
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        // 添加账号
        if (accountManager.addAccountExplicitly(account, null, null)) {
            Log.e(TAG_SYNC, "SyncAccount setting");
            // 设置是否支持同步
            ContentResolver.setIsSyncable(account, AccountProvider.AUTHORITY, 1);
            // 设置是否在收到网络问题时同步
            ContentResolver.setSyncAutomatically(account, AccountProvider.AUTHORITY, true);
            // 按时间间隔后台自动同步，系统仅会在网络可用时发起同步
            ContentResolver.addPeriodicSync(account, AccountProvider.AUTHORITY, Bundle.EMPTY, SYNC_FREQUENCY);
        } else {
            Log.e(TAG_SYNC, "SyncAccount already exited");
        }
    }

    /**
     * 手动触发同步
     */
    public static void triggerRefresh() {
        Bundle b = new Bundle();
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(GetAccount(), AccountProvider.AUTHORITY, b);
    }

    /**
     * Obtain a handle to the {@link android.accounts.Account} used for sync in this application.
     *
     * @return Handle to application's account (not guaranteed to resolve unless CreateSyncAccount()
     * has been called)
     */
    public static Account GetAccount() {
        // Note: Normally the account name is set to the user's identity (username or email
        // address). However, since we aren't actually using any user accounts, it makes more sense
        // to use a generic string in this case.
        //
        // This string should *not* be localized. If the user switches locale, we would not be
        // able to locate the old account, and may erroneously register multiple accounts.
        final String accountName = ACCOUNT_NAME;
        return new Account(accountName, ACCOUNT_TYPE);
    }
}
