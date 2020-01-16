package com.zhangwx.z_utils.Z_AIDL;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

//import com.zhangwx.z_utils.ITestInterface;
import com.zhangwx.z_utils.ITestInterface;
import com.zhangwx.z_utils.R;

public class AidlClientActivity extends AppCompatActivity {
    public static final int identify = 10010;

    private ITestInterface iTestInterface;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl);

        textView = findViewById(R.id.infoText);


        Intent intent = new Intent(this, RemoteServer.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iTestInterface = ITestInterface.Stub.asInterface(service);
            try {
                final String content = iTestInterface.getTestContent(identify);

                ContentData testData = new ContentData();
                testData.content = "test";

                // in 关键字数据流向，只能从客户端发送到服务端，服务端能收到 test，但客户端收不到修改后的内容
                iTestInterface.testIn(testData);
                // out 关键字数据流向，只能从服务端发送到客户端，服务端不能收到 test，收到一个新对象
                iTestInterface.testOut(testData);
                // inout 关键字数据流向，双端通信
                iTestInterface.testInOut(testData);


                textView.setText(testData.content);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
