package com.zhangwx.z_utils.Z_Spi;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zhangwx.spiinterface.Display;

import java.util.Iterator;
import java.util.ServiceLoader;

public class SpiTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceLoader<Display> loader = ServiceLoader.load(Display.class, Display.class.getClassLoader());
        Iterator<Display> iterator = loader.iterator();
        while (iterator.hasNext()) {
            Toast.makeText(this, iterator.next().getDisplayName(), Toast.LENGTH_LONG).show();
        }
    }
}
