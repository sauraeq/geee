package com.geelong.user.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.R
import com.geelong.user.Response.NotificationData
import com.geelong.user.Response.TripHistoryData
import java.util.*

class NotificationAdapter(var mContext: Context,var mlist: List<NotificationData>): RecyclerView
.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        ///   lateinit var nameof: TextView
        // lateinit var time: TextView
        lateinit var create_datee:TextView
        lateinit var titleee:TextView
        lateinit var descri:TextView


        init {
            // nameof=itemView.findViewById(R.id.notification_description)
            // time=itemView.findViewById(R.id.notification_time)
            create_datee=itemView.findViewById(R.id.create_date)
            titleee=itemView.findViewById(R.id.tittle)

            descri=itemView.findViewById(R.id.description)



        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        lateinit var  aContext: Context
        val v= LayoutInflater.from(parent.context).inflate(R.layout.notification_details,parent,false)
        aContext=parent.context

        return ViewHolder(v)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //  holder.nameof.text=notiData[position].description
        //holder.time.text=notiData[position].time
        holder.create_datee.text=mlist[position].created_date
        holder.titleee.text=mlist[position].title
        var descri=mlist[position].description
        holder.descri.text=getCapsSentences(descri)
    }

    override fun getItemCount(): Int {

        return mlist.size
    }

    private fun getCapsSentences(tagName: String): String? {
        val splits = tagName.lowercase(Locale.getDefault()).split(" ".toRegex()).toTypedArray()
        val sb = StringBuilder()

        try
        {
            for (i in splits.indices) {
                val eachWord = splits[i]
                if (i > 0 && eachWord.length > 0) {
                    sb.append(" ")
                }
                val cap = (eachWord.substring(0, 1).uppercase(Locale.getDefault())
                        + eachWord.substring(1))
                sb.append(cap)
            }
        } catch(e:Exception)
        {

        }

        return sb.toString()
    }

}
