package dev.gafoor.joysticklauncher.home

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class NotificationShadeService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        // Not needed for just expanding the shade
    }

    override fun onInterrupt() {
        // Handle interruption if needed
    }

    fun expandNotifications() {
        performGlobalAction(GLOBAL_ACTION_NOTIFICATIONS)
    }

    companion object {
        var instance: NotificationShadeService? = null
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
    }
}