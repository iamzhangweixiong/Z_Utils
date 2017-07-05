package com.zhangwx.z_utils.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import static com.zhangwx.z_utils.Z_Pub.DebugUtils.TAG_SYNC;

/**
 * Created by zhangwx
 * on 2017/7/3.
 */

public class AccountAuthenticator extends AbstractAccountAuthenticator {

    public AccountAuthenticator(Context context) {
        super(context);
        Log.e(TAG_SYNC, "AccountAuthenticator");
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                 String s) {
        Log.e(TAG_SYNC, "AccountAuthenticator editProperties");
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse,
                             String s, String s2, String[] strings, Bundle bundle)
            throws NetworkErrorException {
        Log.e(TAG_SYNC, "AccountAuthenticator addAccount");
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                     Account account, Bundle bundle)
            throws NetworkErrorException {
        Log.e(TAG_SYNC, "AccountAuthenticator confirmCredentials");
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse,
                               Account account, String s, Bundle bundle)
            throws NetworkErrorException {
        Log.e(TAG_SYNC, "AccountAuthenticator getAuthToken");
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAuthTokenLabel(String s) {
        Log.e(TAG_SYNC, "AccountAuthenticator getAuthTokenLabel");
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                    Account account, String s, Bundle bundle)
            throws NetworkErrorException {
        Log.e(TAG_SYNC, "AccountAuthenticator updateCredentials");
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse,
                              Account account, String[] strings)
            throws NetworkErrorException {
        Log.e(TAG_SYNC, "AccountAuthenticator hasFeatures");
        throw new UnsupportedOperationException();
    }
}
