package com.willis.ai_assistant3

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.WindowManager
import cn.jiguang.verifysdk.api.JVerificationInterface
import com.willis.base.installBaseLibrary

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/12
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        installBaseLibrary(this)
        JVerificationInterface.init(this)
        JVerificationInterface.setDebugMode(true)

        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }

        })
    }
}