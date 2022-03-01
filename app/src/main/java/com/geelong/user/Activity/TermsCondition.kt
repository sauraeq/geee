package com.geelong.user.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.geelong.user.R

class TermsCondition : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_condition)

        var back_activity=findViewById<ImageView>(R.id.back_tc_activity_arrow)

        supportActionBar?.hide()

        back_activity.setOnClickListener {
            val intent = Intent(this, Search1::class.java)
            startActivity(intent)
        }
    }
}