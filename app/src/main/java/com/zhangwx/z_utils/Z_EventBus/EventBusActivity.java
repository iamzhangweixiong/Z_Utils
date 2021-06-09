package com.zhangwx.z_utils.Z_EventBus;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zhangwx.z_utils.R;

import org.greenrobot.eventbus.EventBus;

public class EventBusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbus);

        EventBusProvider.init();

        findViewById(R.id.sendEvent).setOnClickListener(v -> {
            EventBus.getDefault().postSticky("event from EventBusActivity");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(this, EventBus2Activity.class));
        });

        findViewById(R.id.sendRxBusEvent).setOnClickListener(v -> {

        });
    }

}
