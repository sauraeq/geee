package com.geelong.user.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.R
import com.geelong.user.Response.DataXX
import com.geelong.user.Response.TripHistoryData

class TripAdapter(var mContext: Context,var mlist: List<TripHistoryData>) : RecyclerView.Adapter<TripAdapter.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {


        lateinit var user_image:ImageView
        lateinit var user_name:TextView
        lateinit var user_distance_travel:TextView
        lateinit var user_fare:TextView
        lateinit var user_trip_time:TextView
        lateinit var user_pick_up_pick:TextView
        lateinit var user_drop_location:TextView



        init {

            user_image=itemView.findViewById(R.id.user_image_trip)
            user_name=itemView.findViewById(R.id.user_profile_trip)
            user_distance_travel=itemView.findViewById(R.id.user_distance_travel_trip)
            user_fare=itemView.findViewById(R.id.user_price_trip)
            user_trip_time=itemView.findViewById(R.id.user_time_trip)
            user_pick_up_pick=itemView.findViewById(R.id.user_pickup_trip)
            user_drop_location=itemView.findViewById(R.id.user_drop_trip)

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        lateinit var  aContext: Context
        val v= LayoutInflater.from(parent.context).inflate(R.layout.trip_details,parent,false)
        aContext=parent.context

        return ViewHolder(v)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.user_name.text=mlist[position].driver_name
        holder.user_distance_travel.text=mlist[position].distance
        holder.user_fare.text=mlist[position].amount
        holder.user_trip_time.text=mlist[position].created_date
        holder.user_pick_up_pick.text=mlist[position].pickup_address
        holder.user_drop_location.text=mlist[position].drop_address

    }

    override fun getItemCount(): Int {

        return mlist.size
    }

}
