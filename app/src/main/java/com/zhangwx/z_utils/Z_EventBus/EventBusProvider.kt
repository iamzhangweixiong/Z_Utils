package com.zhangwx.z_utils.Z_EventBus

import org.greenrobot.eventbus.EventBus

object EventBusProvider {

    @JvmStatic
    lateinit var eventBus: EventBus

    @JvmStatic
    fun init() {
        eventBus = EventBus.builder()
                .throwSubscriberException(true)
                .installDefaultEventBus()
    }
}