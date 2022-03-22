package com.geelong.user.Activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.geelong.user.Fragment.ConfirmFragment
import com.geelong.user.R

class Confirm : AppCompatActivity() {
    lateinit var  customprogress:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)
        supportActionBar?.hide()
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loader_layout)
        customprogress.show()


        val bundle = Bundle()
        bundle.putString("fragmentName", "Settings Fragment")
        val settingsFragment = ConfirmFragment()
        settingsFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_content_id1, settingsFragment).commit()
                customprogress.hide()
    }
    fun inte()
    {
        onBackPressed()
    }
}