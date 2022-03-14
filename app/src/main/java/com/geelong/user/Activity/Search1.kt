package com.geelong.user.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customnavigationdrawerexample.ClickListener
import com.geelong.user.Model.NavigationItemModel
import com.geelong.user.Adapter.NavigationRVAdapter
import com.example.customnavigationdrawerexample.RecyclerTouchListener
import com.geelong.user.Fragment.HomeFragment
import com.geelong.user.R
import com.geelong.user.Util.SharedPreferenceUtils


class Search1 : AppCompatActivity() {

    lateinit var logout_btn:LinearLayout

    lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: NavigationRVAdapter
    lateinit var navigation_rv: RecyclerView
    lateinit var ivClose1:ImageView


    private var items = arrayListOf(
        NavigationItemModel(R.drawable.home, "Account"),
        NavigationItemModel(R.drawable.trips, "Trips"),
        NavigationItemModel(R.drawable.noti, "Notifications"),
        NavigationItemModel(R.drawable.tc, "Terms & Conditions"),
        NavigationItemModel(R.drawable.privacy, "Privacy Policy")
       // NavigationItemModel(R.drawable.home, "Profile"),
        // NavigationItemModel(R.drawable.back, "Like us on facebook")
    )
    private var items1 = arrayListOf(
        NavigationItemModel(R.drawable.home, "Account"),
        NavigationItemModel(R.drawable.trips, "Trips"),
        NavigationItemModel(R.drawable.noti, "Notifications"),
        NavigationItemModel(R.drawable.tc, "Terms & Conditions"),
        NavigationItemModel(R.drawable.privacy, "Privacy Policy")
       // NavigationItemModel(R.drawable.home, "Profile"),
        // NavigationItemModel(R.drawable.back, "Like us on facebook")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search1)
        supportActionBar?.hide()
       // window.setStatusBarColor(ContextCompat.getColor(this,android.R.color.transparent))
        drawerLayout = findViewById(R.id.drawer_layout)
        navigation_rv=findViewById(R.id.navigation_rv1)
        var ivMenu=findViewById<ImageView>(R.id.ivMenu1)
        ivClose1=findViewById(R.id.ivClose)

        logout_btn=findViewById(R.id.Logout_Linear_Layout)


       /* img_prfil.setOnClickListener() {
            intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        */
        val bundle = Bundle()
        bundle.putString("fragmentName", "Settings Fragment")
        val settingsFragment = HomeFragment()
        settingsFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_main_content_id, settingsFragment).commit()
        ivClose1.setOnClickListener() {
            drawerLayout.closeDrawer(GravityCompat.START)
        }


        // Set the toolbar
        // setSupportActionBar(activity_main_toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(false);

        // Setup Recyclerview's Layout
        navigation_rv.layoutManager = LinearLayoutManager(this)
        navigation_rv.setHasFixedSize(true)
        ivMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
       }

        logout_btn.setOnClickListener {
            SharedPreferenceUtils.getInstance(this)?.clear()
            val intent = Intent(this@Search1, Sign_Up::class.java)
            startActivity(intent)
        }

        // Add Item Touch Listener
        navigation_rv.addOnItemTouchListener(RecyclerTouchListener(this, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> {
                        val intent = Intent(this@Search1, Acccount::class.java)
                        startActivity(intent)

                    }
                    1 -> {
                      //  drawerLayout.closeDrawer(GravityCompat.START)
                        val intent = Intent(this@Search1, TripDetails::class.java)
                        startActivity(intent)
                    }
                    2 -> {
                        val intent = Intent(this@Search1, Notification::class.java)
                        startActivity(intent)
                    }
                    3 -> {
                        val intent = Intent(this@Search1, TermsCondition::class.java)
                        startActivity(intent)
                        // # Books Fragment

                    }
                    4 -> {
                        // # Profile Activity
                        val intent = Intent(this@Search1, Privacy_Policy::class.java)
                        startActivity(intent)
                    }
                    5 -> {
                        drawerLayout.closeDrawer(GravityCompat.START)
                       // val intent = Intent(this@MainActivity1, ProfileActivity::class.java)
                       // startActivity(intent)
                        // # Settings Fragment
                        /* val bundle = Bundle()
                         bundle.putString("fragmentName", "Settings Fragment")
                         val settingsFragment = DemoFragment()
                         settingsFragment.arguments = bundle
                         supportFragmentManager.beginTransaction()
                             .replace(R.id.activity_main_content_id, settingsFragment).commit()*/

                    }
                    6 -> {


                    }
                }
                // Don't highlight the 'Profile' and 'Like us on Facebook' item row
                updateAdapter(position)
                if (position != 6 && position != 4) {

                }
                Handler().postDelayed({
                    drawerLayout.closeDrawer(GravityCompat.START)
                }, 200)
            }
        }))

        // Update Adapter with item data and highlight the default menu item ('Home' Fragment)
        updateAdapter(0)

        // Set 'Home' as the default fragment when the app starts
        /*  val bundle = Bundle()
          bundle.putString("fragmentName", "Home Fragment")
          val homeFragment = DemoFragment()
          homeFragment.arguments = bundle
          supportFragmentManager.beginTransaction()
              .replace(R.id.activity_main_content_id, homeFragment).commit()*/


        // Close the soft keyboard when you open or close the Drawer
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {
                // Triggered once the drawer closes
                super.onDrawerClosed(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                // Triggered once the drawer opens
                super.onDrawerOpened(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()


        // Set Header Image
        // navigation_header_img.setImageResource(R.drawable.logo)

        // Set background of Drawer
       // navigation_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun updateAdapter(highlightItemPos: Int) {
        adapter = NavigationRVAdapter(items,items1, highlightItemPos)
        navigation_rv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Checking for fragment count on back stack
            if (supportFragmentManager.backStackEntryCount > 0) {
                // Go to the previous fragment
                supportFragmentManager.popBackStack()
            } else {
                // Exit the app
                super.onBackPressed()
            }
        }
    }

    fun click1(){
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun inte()
    {
        val intent = Intent(this, Confirm::class.java)
        startActivity(intent)
    }




}