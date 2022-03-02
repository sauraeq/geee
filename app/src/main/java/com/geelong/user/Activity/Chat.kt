package com.geelong.user.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.geelong.user.R

class Chat : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        supportActionBar?.hide()
    }
}