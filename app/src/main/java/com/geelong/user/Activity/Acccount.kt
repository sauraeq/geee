package com.geelong.user.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import com.geelong.user.R

class Acccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acccount)
        var edit_profile=findViewById<ImageView>(R.id.edit_profile_imageview)
        var back_act=findViewById<ImageView>(R.id.back_activity)
        edit_profile.setOnClickListener {
            val intent = Intent(this, AccountEdit::class.java)
            startActivity(intent)
        }
        supportActionBar?.hide()
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        back_act.setOnClickListener {
            val intent = Intent(this, Search1::class.java)
            startActivity(intent)
        }

    }
}