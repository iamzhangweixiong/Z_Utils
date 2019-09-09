package com.zhangwx.z_utils.Z_AIDL;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

//import com.zhangwx.z_utils.ITestInterface;

public class RemoteServer extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
//
//    private final ITestInterface.Stub binder = new ITestInterface.Stub() {
//
//        @Override
//        public String getTestContent(int id) throws RemoteException {
//
//        }
//    };
}
