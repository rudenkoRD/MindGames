package com.developer.rrd_projects
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationHelper = NotificationHelper(context)
        notificationHelper.createNotification()
    }

}
 