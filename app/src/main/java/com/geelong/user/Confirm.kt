package com.geelong.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat

class Confirm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)
        supportActionBar?.hide()

        val bundle = Bundle()
        bundle.putString("fragmentName", "Settings Fragment")
        val settingsFragment = ConfirmFragment()
        settingsFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_content_id1, settingsFragment).commit()

    }
    fun inte()
    {
        val intent = Intent(this,ConfirmPick_up::class.java)
        startActivity(intent)
    }
}