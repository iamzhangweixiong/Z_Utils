package com.zhangwx.z_utils.Z_Thread.HandlerThread;

import android.os.MessageQueue;

public class IdleHandlerTest {

    MessageQueue.IdleHandler idleHandler = new MessageQueue.IdleHandler() {
        @Override
        public boolean queueIdle() {
            return false;
        }
    };
}
