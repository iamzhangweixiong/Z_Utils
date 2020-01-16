package com.zhangwx.z_utils.Z_LifeCycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.LifecycleOwner

class LifeCycleObserverTest: LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clear() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onLifecycleChanged(owner: LifecycleOwner,event: Lifecycle.Event) {

    }
}