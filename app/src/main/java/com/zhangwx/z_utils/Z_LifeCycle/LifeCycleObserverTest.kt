package com.zhangwx.z_utils.Z_LifeCycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.LifecycleOwner

class LifeCycleObserverTest: LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clear() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onLifecycleChanged(owner: LifecycleOwner,event: Lifecycle.Event) {

    }
}