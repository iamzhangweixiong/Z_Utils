package com.zhangwx.z_utils.Z_AIDL;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.zhangwx.z_utils.ITestInterface;

public class RemoteServer extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final ITestInterface.Stub binder = new ITestInterface.Stub() {

        @Override
        public String getTestContent(int id) throws RemoteException {
            switch (id) {
                case AidlClientActivity.identify:
                    return "RemoteServer";
            }
            return "";
        }

        @Override
        public void testIn(ContentData data) throws RemoteException {
            data.content = "testIn from server";
        }

        @Override
        public void testOut(ContentData data) throws RemoteException {
            data.content = "testOut from server";
        }

        @Override
        public void testInOut(ContentData data) throws RemoteException {
            data.content = "testInOut from server";
        }
    };
}
