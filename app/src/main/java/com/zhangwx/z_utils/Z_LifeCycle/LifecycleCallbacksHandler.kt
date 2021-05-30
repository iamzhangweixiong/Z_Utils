package com.zhangwx.z_utils.Z_LifeCycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.ref.WeakReference
import java.util.*

object LifecycleCallbacksHandler : Application.ActivityLifecycleCallbacks {
  private var mFirstActivityLaunched = false
  private val mAliveActivities = LinkedHashMap<Int, String>()
  private var mCurrentActivityReference: WeakReference<Activity>? = null
  private var mFirstActivityObserver: ((activity: Activity) -> Unit)? = null

  @JvmStatic
  var isInBackground = false

  override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    mAliveActivities[activity.hashCode()] = activity.localClassName
    checkAndCreateActivityReference(activity)
    if (!mFirstActivityLaunched) {
      mFirstActivityLaunched = true
      mFirstActivityObserver?.invoke(activity)
    }
  }

  override fun onActivityResumed(activity: Activity) {
    checkAndCreateActivityReference(activity)
  }

  override fun onActivityPaused(activity: Activity) {
  }

  override fun onActivityStarted(activity: Activity) {
    isInBackground = false
    checkAndCreateActivityReference(activity)
  }

  override fun onActivityStopped(activity: Activity) {
    isInBackground = true
  }

  override fun onActivityDestroyed(activity: Activity) {
    mAliveActivities.remove(activity.hashCode())
  }

  override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
  }

  @JvmStatic
  fun getCurrentActivity() = mCurrentActivityReference?.get()

  fun setFirstActivityObserver(firstActivityObserver: (activity: Activity) -> Unit) {
    mFirstActivityObserver = firstActivityObserver
  }

  private fun checkAndCreateActivityReference(activity: Activity) {
    if (mCurrentActivityReference == null || mCurrentActivityReference?.get() != activity) {
      mCurrentActivityReference = WeakReference(activity)
    }
  }
}