package com.geelong.user.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.Adapter.TripAdapter
import com.geelong.user.R

class TripDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)
        supportActionBar?.hide()
        var back_activity=findViewById<ImageView>(R.id.LeftArrow)

        val recyclerview = findViewById<RecyclerView>(R.id.rcyView_trip_details)
        recyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = TripAdapter()
        recyclerview.adapter = adapter
        back_activity.setOnClickListener {
            val intent = Intent(this, Search1::class.java)
            startActivity(intent)
        }

    }
}