package com.geelong.user.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.geelong.user.Activity.Acccount
import com.geelong.user.R

class AccountEdit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_edit)
        var  save=findViewById<TextView>(R.id.edit_profile_save)

        var back_act=findViewById<ImageView>(R.id.back_activity1)

        supportActionBar?.hide()
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        save.setOnClickListener {
            val intent = Intent(this, Acccount::class.java)
            startActivity(intent)
        }
        back_act.setOnClickListener {
           onBackPressed()
        }
    }
}