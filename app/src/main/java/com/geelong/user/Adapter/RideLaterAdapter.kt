package com.geelong.user.Adapter

import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.Activity.RideLaterHistory
import com.geelong.user.Activity.RideLaterOverview
import com.geelong.user.R
import com.geelong.user.Response.PivacyData

class RideLaterAdapter(var mContext: Context) : RecyclerView.Adapter<RideLaterAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        ///   lateinit var nameof: TextView
        // lateinit var time: TextView

       /* lateinit var introduction: TextView
        lateinit var description: TextView*/
        lateinit var ride_edit:TextView


        init {
            // nameof=itemView.findViewById(R.id.notification_description)
            // time=itemView.findViewById(R.id.notification_time)

          /*  introduction=itemView.findViewById(R.id.privacy_heading)
            description=itemView.findViewById(R.id.privacy_description)*/
            ride_edit=itemView.findViewById(R.id.ride_adapter_edit_btn)


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
        //  holder.nameof.text=notiData[position].description
        //holder.time.text=notiData[position].time

       /* holder.description.text= Html.fromHtml(mlist[position].description)
        holder.introduction.text=mlist[position].id*/
        holder.ride_edit.setOnClickListener {
            var intent=Intent(mContext,RideLaterOverview::class.java)
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return 10
    }

}