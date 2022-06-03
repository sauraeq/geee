package com.geelong.user.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.API.APIUtils
import com.geelong.user.R
import com.geelong.user.Response.CancelTripHistoryResData
import com.geelong.user.Response.CancelTripHistoryResponse
import com.geelong.user.Response.TripHistoryData
import com.geelong.user.ReveiwResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CancelTripHistoryAdapter (var mContext: Context, var mlist: List<CancelTripHistoryResData>) :
    RecyclerView
.Adapter<CancelTripHistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {


        lateinit var user_image:ImageView
        lateinit var user_name:TextView
        lateinit var user_distance_travel:TextView
        lateinit var user_fare:TextView
        lateinit var user_trip_time:TextView
        lateinit var user_pick_up_pick:TextView
        lateinit var user_drop_location:TextView

        var booking_id:String=""
        var user_idd:String=""
        var driver_id_id:String=""



        init {

            user_image=itemView.findViewById(R.id.user_image_trip_cancel)
            user_name=itemView.findViewById(R.id.user_profile_trip_cancel)
            user_distance_travel=itemView.findViewById(R.id.user_distance_travel_trip_cancel)
            user_fare=itemView.findViewById(R.id.user_price_trip_cancel)
            user_trip_time=itemView.findViewById(R.id.user_time_trip_cancel)
            user_pick_up_pick=itemView.findViewById(R.id.user_pickup_trip_cancel)
            user_drop_location=itemView.findViewById(R.id.user_drop_trip_cancel)



        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        lateinit var  aContext: Context
        val v= LayoutInflater.from(parent.context).inflate(R.layout.cancel_trip_history,parent,false)
        aContext=parent.context

        return ViewHolder(v)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.booking_id=mlist[position].id
        holder.driver_id_id=mlist[position].driver_id
        holder.user_idd=mlist[position].uid
        var img_url=mlist[position].driver_photo
         var username=mlist[position].driver_name
        holder.user_name.text=getCapsSentences(username)
        holder.user_distance_travel.text=mlist[position].distance+" "+"Km"
        holder.user_fare.text="$"+mlist[position].amount
        holder.user_trip_time.text=mlist[position].created_date
        holder.user_pick_up_pick.text=mlist[position].pickup_address
        holder.user_drop_location.text=mlist[position].drop_address

        if (img_url.isEmpty())
        {
            var pica=Picasso.get()
            pica.load(R.drawable.defaultt).into(holder.user_image)
        }
        else
        {
            var pica=Picasso.get()
            pica.load(img_url).into(holder.user_image)
        }

     /*   holder.itemView.setOnClickListener()
        {

            Toast.makeText(mContext,position.toString(),Toast.LENGTH_LONG).show()


        }*/


    }

    override fun getItemCount(): Int {

        return mlist.size
    }

    private fun getCapsSentences(tagName: String): String? {
        val splits = tagName.lowercase(Locale.getDefault()).split(" ".toRegex()).toTypedArray()
        val sb = StringBuilder()
        try {
            for (i in splits.indices) {
                val eachWord = splits[i]
                if (i > 0 && eachWord.length > 0) {
                    sb.append(" ")
                }
                val cap = (eachWord.substring(0, 1).uppercase(Locale.getDefault())
                        + eachWord.substring(1))
                sb.append(cap)
            }
        }
        catch (e:Exception)
        {

        }

        return sb.toString()
    }




}
