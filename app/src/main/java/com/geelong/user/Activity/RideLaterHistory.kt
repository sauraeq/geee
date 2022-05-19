package com.geelong.user.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.geelong.user.Adapter.RideLaterAdapter
import com.geelong.user.R
import kotlinx.android.synthetic.main.activity_ride_later_history.*

class RideLaterHistory : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ride_later_history)

        rcyView_ride_later.layoutManager = LinearLayoutManager(this)

        val adapter = RideLaterAdapter(this)
        rcyView_ride_later.adapter = adapter
    }
}