package com.geelong.user.Activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customnavigationdrawerexample.ClickListener
import com.example.customnavigationdrawerexample.RecyclerTouchListener
import com.geelong.user.API.APIUtils
import com.geelong.user.Adapter.NavigationRVAdapter
import com.geelong.user.Fragment.DriverFragments

import com.geelong.user.Model.NavigationItemModel
import com.geelong.user.R
import com.geelong.user.Response.LoginResponse
import com.geelong.user.Response.NewNotificationResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.NetworkUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_driver_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class DriverDetails : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: NavigationRVAdapter
    lateinit var navigation_rv: RecyclerView
    lateinit var ivClose1: ImageView
    lateinit var customprogress:Dialog


    private var items = arrayListOf(
        NavigationItemModel(R.drawable.home, "Account"),
        NavigationItemModel(R.drawable.trips, "Trips"),
        NavigationItemModel(R.drawable.noti, "Notifications"),
        NavigationItemModel(R.drawable.tc, "Terms & Conditions"),
        NavigationItemModel(R.drawable.privacy, "Privacy Policy")

    )
    private var items1 = arrayListOf(
        NavigationItemModel(R.drawable.home, "Account"),
        NavigationItemModel(R.drawable.trips, "Trips"),
        NavigationItemModel(R.drawable.noti, "Notifications"),
        NavigationItemModel(R.drawable.tc, "Terms & Conditions"),
        NavigationItemModel(R.drawable.privacy, "Privacy Policy")

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_details)
        supportActionBar?.hide()
        drawerLayout = findViewById(R.id.drawer_layout1)
       navigation_rv=findViewById(R.id.navigation_rv11)

       ivClose1=findViewById(R.id.ivClose)
        customprogress= Dialog(this)
        customprogress.setContentView(R.layout.loader_layout)
        NewNotification()

        val intent = intent
        val message = intent.getStringExtra("message")
        if(!message.isNullOrEmpty()) {
            AlertDialog.Builder(this)
                    .setTitle("Notification")
                    .setMessage(message)
                    .setPositiveButton("Ok", { dialog, which -> }).show()
        }

        logout_driver_details_linear.setOnClickListener {
            SharedPreferenceUtils.getInstance(this)?.clear()
            val intent = Intent(this, Sign_Up::class.java)
            startActivity(intent)
        }

  if (NetworkUtils.checkInternetConnection(this))
  {
      progilepic()
      customprogress.show()
  }


    val bundle = Bundle()
    bundle.putString("fragmentName", "Settings Fragment")
    val settingsFragment = DriverFragments()
    settingsFragment.arguments = bundle
    supportFragmentManager.beginTransaction()
    .replace(R.id.activity_main_content_id, settingsFragment).commit()
        var ivMenu=findViewById<ImageView>(R.id.ivMenu_driver)
    ivClose1.setOnClickListener() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }



    getSupportActionBar()?.setDisplayShowTitleEnabled(false);
    getSupportActionBar()?.setDisplayHomeAsUpEnabled(false);


    navigation_rv.layoutManager = LinearLayoutManager(this)
    navigation_rv.setHasFixedSize(true)
    ivMenu.setOnClickListener {
        drawerLayout.openDrawer(GravityCompat.START)
    }


    navigation_rv.addOnItemTouchListener(RecyclerTouchListener(this, object : ClickListener {
        override fun onClick(view: View, position: Int) {
            when (position) {
                0 -> {
                    val intent = Intent(this@DriverDetails, Acccount::class.java)
                    startActivity(intent)

                }
                1 -> {

                    val intent = Intent(this@DriverDetails, TripDetails::class.java)
                    startActivity(intent)
                }
                2 -> {
                    val intent = Intent(this@DriverDetails, Notification::class.java)
                    startActivity(intent)
                }
                3 -> {
                    val intent = Intent(this@DriverDetails, TermsCondition::class.java)
                    startActivity(intent)

                }
                4 -> {

                    val intent = Intent(this@DriverDetails, Privacy_Policy::class.java)
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

            updateAdapter(position)
            if (position != 6 && position != 4) {

            }
            Handler().postDelayed({
                drawerLayout.closeDrawer(GravityCompat.START)
            }, 200)
        }
    }))


    updateAdapter(0)

    val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
        this,
        drawerLayout,
        R.string.navigation_drawer_open,
        R.string.navigation_drawer_close
    ) {
        override fun onDrawerClosed(drawerView: View) {

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

        if (supportFragmentManager.backStackEntryCount > 0) {

            supportFragmentManager.popBackStack()
        } else {

            super.onBackPressed()
        }
    }
}

fun click(){
    drawerLayout.openDrawer(GravityCompat.START)
}

fun inte()
{
    val intent = Intent(this, Confirm::class.java)
    startActivity(intent)
}

    fun progilepic()
    {
       var token_id=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils
               .TokenId,"")
                .toString()
        var mobi_num=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils.User_Mobile_Number,"").toString()
        val request = HashMap<String, String>()
        request.put("mobile",mobi_num)
        request.put("device_tokanid",token_id)



        var login_in: Call<LoginResponse> = APIUtils.getServiceAPI()!!.Login(request)

        login_in.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                try {

                           customprogress.hide()
                    if (response.body()!!.success.equals("true")) {
                        if (response.body()!!.data.isEmpty())
                        {
                            Toast.makeText(this@DriverDetails,"DATA Not Found",Toast.LENGTH_LONG).show()

                        }
                        else
                        {
                            var img_url=response.body()!!.data[0].profile_photo
                            tvName_sidebar.text=response.body()!!.data[0].name
                            user_location_se.text=response.body()!!.data[0].address

                            if(img_url.isEmpty())
                            {
                                val picasso=Picasso.get()
                                picasso.load(R.drawable.driverimg).into(navigation_header_img)
                            }
                            else{
                                val picasso= Picasso.get()
                                picasso.load(img_url).into(navigation_header_img)
                            }
                        }




                        customprogress.hide()


                    } else {

                        customprogress.hide()
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    Toast.makeText(this@DriverDetails,e.message, Toast.LENGTH_LONG).show()
                    customprogress.hide()

                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                customprogress.hide()
            }

        })
    }

    fun GoTo()
    {
        var intent=Intent(this,CancelTrip::class.java)
        startActivity(intent)
    }

    fun NewNotification()
    {

        var user_id=SharedPreferenceUtils.getInstance(this)?.getStringValue(ConstantUtils
            .USER_ID,"")
            .toString()

        val request = HashMap<String, String>()
        request.put("user_id",user_id)




        var new_Noti: Call<NewNotificationResponse> = APIUtils.getServiceAPI()!!.NewNotification(request)

        new_Noti.enqueue(object : Callback<NewNotificationResponse> {
            override fun onResponse(call: Call<NewNotificationResponse>, response: Response<NewNotificationResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {
                        /*Toast.makeText(this@Search1,response.body()!!.data[0].count+"seracg1",Toast
                            .LENGTH_LONG)
                            .show()*/
                        SharedPreferenceUtils.getInstance(this@DriverDetails)!!.setStringValue(ConstantUtils.Total_notificat_count,response.body()!!.data[0].count)
                            .toString()


                        /*      customProgress.hide()*/


                    } else {

                        //  Toast.makeText(this@Search1,"Error", Toast.LENGTH_LONG).show()
                        /*customProgress.hide()*/
                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())

                    Toast.makeText(this@DriverDetails,e.message, Toast.LENGTH_LONG).show()
                    /* customProgress.hide()
 */
                }

            }

            override fun onFailure(call: Call<NewNotificationResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())

                Toast.makeText(this@DriverDetails,t.message, Toast.LENGTH_LONG).show()
                /* customProgress.hide()*/
            }

        })
    }





}