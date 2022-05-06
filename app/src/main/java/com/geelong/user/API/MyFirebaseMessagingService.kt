package com.geelong.user.API

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.view.ViewGroup
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.geelong.user.Activity.DriverDetails
import com.geelong.user.Activity.Search1
import com.geelong.user.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val channelID="notification_channel"
const val channelName="com.geelong.user"

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if(remoteMessage.getNotification()!=null){
            generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)

        }
       // showDialog()
        /*val intent = Intent(this@MyFirebaseMessagingService, DriverDetails::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("message", remoteMessage.notification?.body!!)
        startActivity(intent)*/
    }

    @SuppressLint("RemoteViewLayout")
    fun getRemoteview(title: String,message: String): RemoteViews {
        val remoteView= RemoteViews("com.geelong.user",R.layout.notification)
        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.message,message)
        remoteView.setImageViewResource(R.id.icon_notification,R.drawable.appicon)
        return remoteView
    }


    fun generateNotification(title:String,message:String)
    {


//      Toast.makeText(this,title+message,Toast.LENGTH_LONG).show()
        /*  val intent= Intent(this,Search1::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent= PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT)*/

        // channel id, channel name
        var builder: NotificationCompat.Builder= NotificationCompat.Builder(applicationContext,
            channelID


        )
            .setSmallIcon(R.drawable.appicon)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
        builder=builder.setContent(getRemoteview(title,message))

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val notificationChannel=
                NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)


        }
        notificationManager.notify(0,builder.build())
       /* val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val mp: MediaPlayer = MediaPlayer.create(this, notification)
        mp.start()*/

    }

    private fun showDialog() {
        val dialog = Dialog(baseContext)

        dialog.getWindow()!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_write_review)

        dialog.show()

    }

}