package com.zhangwx.z_utils.Z_Widget.password;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.zhangwx.z_utils.R;

import java.util.List;

/**
 * Created by zhangweixiong on 2017/11/2.
 */

public class PasswordActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        final LockPatternView patternView = (LockPatternView) findViewById(R.id.patternView);
        patternView.setOnPatternListener(new LockPatternView.OnPatternListener() {
            public void onPatternStart() {
            }

            public void onPatternCleared() {
            }

            public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {
            }

            public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                if (pattern.size() < 4) {
                    patternView.clearPattern();
                    Toast.makeText(PasswordActivity.this, "must be > 4", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String password = LockPatternView.patternToString(pattern);
                Toast.makeText(PasswordActivity.this, password, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
