package com.geelong.user.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.Adapter.NotificationAdapter
import com.geelong.user.Adapter.TripAdapter
import com.geelong.user.R

class Notification : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        supportActionBar?.hide()
        var back_activity=findViewById<ImageView>(R.id.LeftArrow1)

        val recyclerview = findViewById<RecyclerView>(R.id.rcyView_trip_notification)
        recyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = NotificationAdapter()
        recyclerview.adapter = adapter

        back_activity.setOnClickListener {

            onBackPressed()
        }

    }
}