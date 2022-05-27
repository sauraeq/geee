package com.geelong.user.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.API.APIUtils
import com.geelong.user.Activity.RideLaterHistory
import com.geelong.user.Activity.RideLaterOverview
import com.geelong.user.Activity.Search1
import com.geelong.user.R
import com.geelong.user.Response.CancelSubmitResponse
import com.geelong.user.Response.RideLaterHistoryRes
import com.geelong.user.Response.RideLaterHistoryResData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RideLaterAdapter(var mContext: Context,var mlist: List<RideLaterHistoryResData>) : RecyclerView
.Adapter<RideLaterAdapter
.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        lateinit var ride_edit:TextView
        lateinit var ride_later_time_txt:TextView
        lateinit var ride_later_date_txt:TextView
        lateinit var ride_later_pass_txt:TextView
        lateinit var ride_later_pickup_txt:TextView
        lateinit var ride_later_drop_txt:TextView
        lateinit var ride_later_cancel_txt:TextView


        init {

            ride_edit=itemView.findViewById(R.id.later_adapter_edit_btn)
            ride_later_time_txt=itemView.findViewById(R.id.later_adapter_time)
            ride_later_date_txt=itemView.findViewById(R.id.later_adapter_date)
            ride_later_pass_txt=itemView.findViewById(R.id.later_adapter_numberofPassenger)
            ride_later_pickup_txt=itemView.findViewById(R.id.later_adapter_pickup)
            ride_later_drop_txt=itemView.findViewById(R.id.later_adapter_drop)
            ride_later_cancel_txt=itemView.findViewById(R.id.later_adapter_cancel_btn)

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        lateinit var  aContext: Context
        val v= LayoutInflater.from(parent.context).inflate(R.layout.ride_later_history_layout,parent,false)
        aContext=parent.context

        return ViewHolder(v)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.ride_later_time_txt.text=mlist[position].time
       holder.ride_later_date_txt.text=mlist[position].date
        holder.ride_later_pass_txt.text=mlist[position].passenger
        holder.ride_later_pickup_txt.text=mlist[position].pickup_address
        holder.ride_later_drop_txt.text=mlist[position].drop_address


        holder.ride_edit.setOnClickListener {
            var intent=Intent(mContext,RideLaterOverview::class.java)
            intent.putExtra("pickup",mlist[position].pickup_address)
            intent.putExtra("drop",mlist[position].drop_address)
            intent.putExtra("no_pass",mlist[position].passenger)
            intent.putExtra("time",mlist[position].time)
            intent.putExtra("des_lat",mlist[position].drop_latitude)
            intent.putExtra("sou_lat",mlist[position].pickup_latitude)
            intent.putExtra("sou_long",mlist[position].pickup_longitude)
            intent.putExtra("booking_id",mlist[position].id)
            intent.putExtra("date",mlist[position].date)

            mContext.startActivity(intent)
        }
        holder. ride_later_cancel_txt.setOnClickListener {
            notifyDataSetChanged()
           // Toast.makeText(mContext,mlist[position].id.toString(),Toast.LENGTH_LONG).show()

            CancelTripSubmit(mlist[position].id.toString())
        }
    }

    override fun getItemCount(): Int {

        return mlist.size
    }

    fun CancelTripSubmit(booking_id:String)
    {

        val request = HashMap<String, String>()
        request.put("booking_id",booking_id)
        request.put("description","hhh")



        var cancel_trip: Call<CancelSubmitResponse> = APIUtils.getServiceAPI()!!.CancelResultSubmission(request)

        cancel_trip.enqueue(object : Callback<CancelSubmitResponse> {
            override fun onResponse(call: Call<CancelSubmitResponse>, response: Response<CancelSubmitResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {

                       /* Toast.makeText(mContext,response.body()!!.msg.toString(), Toast.LENGTH_LONG)
                            .show()*/

                        var intent=Intent(mContext,Search1::class.java)

                           mContext.startActivity(intent)
                        (mContext as Activity).finish()




                    } else {


                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())




                }

            }

            override fun onFailure(call: Call<CancelSubmitResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())




            }

        })
    }

}
