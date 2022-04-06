package com.geelong.user.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.API.APIUtils
import com.geelong.user.Model.NavigationItemModel
import com.geelong.user.R
import com.geelong.user.Response.NewNotificationResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap


class NavigationRVAdapter(private var items: ArrayList<NavigationItemModel>, private var items1: ArrayList<NavigationItemModel>, private var currentPos: Int) :RecyclerView.Adapter<NavigationRVAdapter.NavigationItemViewHolder>() {

    private lateinit var context: Context
    lateinit var navigation_icon:ImageView
    lateinit var navigation_title1:TextView
    lateinit var notification_countt:ImageView
    var notification_cnt:String=""

    class NavigationItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationItemViewHolder {
        context = parent.context
        val navItem = LayoutInflater.from(parent.context).inflate(R.layout.row_nav_drawer, parent, false)
        navigation_icon=navItem.findViewById(R.id.navigation_icon1)
        navigation_title1=navItem.findViewById(R.id.navigation_title1)
        notification_countt=navItem.findViewById(R.id.red_notication)
        return NavigationItemViewHolder(navItem)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: NavigationItemViewHolder, position: Int) {
        // To highlight the selected Item, show different background color
       // navigation_icon=holder.itemId(R.id.navigation_icon1)

        if(position==2) {
            try {
                notification_cnt = SharedPreferenceUtils.getInstance(context)
                    ?.getStringValue(ConstantUtils.Total_notificat_count, "").toString()
                // Toast.makeText(context,notification_cnt.toString(),Toast.LENGTH_LONG).show( )
                if (notification_cnt.toInt() != 0) {
                    notification_countt.visibility = View.VISIBLE
                } else {
                    notification_countt.visibility = View.GONE
                }


            } catch (e: Exception) {

            }
        }
        else
        {
            notification_countt.visibility=View.GONE
        }


        if (position == currentPos) {
            holder.itemView.setBackgroundResource(R.drawable.back_corner)
            navigation_icon.setImageResource(items[position].icon)

        } else {
            //holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            navigation_icon.setImageResource(items[position].icon)
        }


       // holder.itemView.navigation_icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
       // holder.itemView.navigation_title.setTextColor(Color.BLACK)
        //val font = ResourcesCompat.getFont(context, R.font.mycustomfont)
        //holder.itemView.navigation_text.typeface = font
        //holder.itemView.navigation_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20.toFloat())

       navigation_title1.text = items[position].title


    }

}