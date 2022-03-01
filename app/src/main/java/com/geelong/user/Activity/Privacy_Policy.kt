package com.geelong.user.Activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.geelong.user.R


class Privacy_Policy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        supportActionBar?.hide()

        var back_activity_privacy=findViewById<ImageView>(R.id.LeftArrow2)
        back_activity_privacy.setOnClickListener {
            val intent = Intent(this, Search1::class.java)
            startActivity(intent)
        }

    }
}