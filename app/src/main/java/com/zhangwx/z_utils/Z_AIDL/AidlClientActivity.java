package com.zhangwx.z_utils.Z_AIDL;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

//import com.zhangwx.z_utils.ITestInterface;
import com.zhangwx.z_utils.R;

public class AidlClientActivity extends AppCompatActivity {
    public static final int identify = 10010;

//    private ITestInterface iTestInterface;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);

        textView = findViewById(R.id.infoText);


//        Intent intent = new Intent(this, RemoteServer.class);
//        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            iTestInterface = ITestInterface.Stub.asInterface(service);
//            try {
//                final String content = iTestInterface.getTestContent(identify);
//                textView.setText(content);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    };
}
