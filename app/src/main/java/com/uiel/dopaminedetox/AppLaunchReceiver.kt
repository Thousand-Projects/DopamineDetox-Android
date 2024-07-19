package com.uiel.dopaminedetox

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class AppLaunchService : AccessibilityService() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onServiceConnected() {
        super.onServiceConnected()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        serviceInfo = info
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString()
            if (packageName != null) {
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
                val selectedAppPackages = sharedPreferences.getStringSet("selected_app_packages", emptySet()) ?: emptySet()
                Log.d("TEST",selectedAppPackages.toString())
                if (selectedAppPackages.contains(packageName)) {
                    val currentTime = System.currentTimeMillis()
                    val lastLaunchTime = sharedPreferences.getLong(packageName, 0L)
                    val interval = 30 *60 *1000
                    Log.d("TEST",lastLaunchTime.toString())
                    Log.d("TEST1",currentTime.toString())

                    if (currentTime - lastLaunchTime > interval || lastLaunchTime == 0L) {
                        with(sharedPreferences.edit()) {
                            putLong(packageName, currentTime)
                            apply()
                        }
                        val intent = Intent(this, BreathActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        // Handle interrupt event if necessary
    }
}
