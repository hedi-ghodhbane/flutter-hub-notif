package com.expensya.revamped

import android.os.Handler
import android.os.Looper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.expensya.revamped.services.NotificationActionService
import com.expensya.revamped.services.NotificationRegistrationService

class PushNotificationsFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        var token : String? = null
        var notificationRegistrationService : NotificationRegistrationService? = null
        var notificationActionService : NotificationActionService? = null
    }

    override fun onNewToken(token: String) {
        Handler(Looper.getMainLooper()).post {
            PushNotificationsFirebaseMessagingService.token = token
            notificationRegistrationService?.refreshRegistration()
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.data.let {
            Handler(Looper.getMainLooper()).post {
                notificationActionService?.triggerAction(it.getOrDefault("action", null))
            }
        }
    }
}