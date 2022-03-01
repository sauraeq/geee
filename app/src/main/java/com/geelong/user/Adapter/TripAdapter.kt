package com.geelong.user.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.R

class TripAdapter : RecyclerView.Adapter<TripAdapter.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

     ///   lateinit var nameof: TextView
       // lateinit var time: TextView


        init {
           // nameof=itemView.findViewById(R.id.notification_description)
           // time=itemView.findViewById(R.id.notification_time)


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
      //  holder.nameof.text=notiData[position].description
        //holder.time.text=notiData[position].time
    }

    override fun getItemCount(): Int {

        return 10
    }

}
