package com.geelong.user.Adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.R
import com.geelong.user.Response.CancelTripResponsedata
import kotlinx.android.synthetic.main.canceltrippopup.*


class CancelTripAdapter(
    var mContext: Context, val lintener: PractiseInterface,
    var mlist: List<CancelTripResponsedata>)  :
    RecyclerView
.Adapter<CancelTripAdapter.ViewHolder>() {
    private var selectedItemPosition: String = ""

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


        lateinit var Reason: TextView
        lateinit var Linear_Trip:CardView
        var rowindex:Int=-1


        init {

            Reason=itemView.findViewById(R.id.reason_cancel_trip)
            Linear_Trip=itemView.findViewById(R.id.liner_trip)


        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        lateinit var  aContext: Context
        val v= LayoutInflater.from(parent.context).inflate(R.layout.canceltrip_adp,parent,false)
        aContext=parent.context

        return ViewHolder(v)    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        //  holder.nameof.text=notiData[position].description
        //holder.time.text=notiData[position].time
        holder.Reason.text=mlist[position].description
        holder.itemView.setOnClickListener {
            selectedItemPosition=position.toString()
            notifyDataSetChanged()
            lintener.onclick( mlist[position].description)


        }
        if(selectedItemPosition==position.toString())
        {
           // holder.Reason.setTextColor(Color.YELLOW)
            holder.Linear_Trip.setBackgroundColor(Color.GRAY)

        }
        else
        { //  holder.Reason.setTextColor(Color.BLACK)
            holder.Linear_Trip.setBackgroundColor(Color.WHITE)

        }



        /*holder.description.text=mlist[position].description
        holder.introduction.text=mlist[position].id*/
    }

    override fun getItemCount(): Int {

        return mlist.size
    }

    interface PractiseInterface{
        fun onclick(name:String)
    }

    private fun showDialog() {
        val dialog = Dialog(mContext)
        dialog.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dialog.setCancelable(true)
        dialog.setContentView(R.layout.canceltrippopup)
        lateinit var button: LinearLayout


        button = dialog.findViewById(R.id.payment_success)

        button.setOnClickListener {
            dialog.dismiss()

        }
        dialog.cancal_popup_img.setOnClickListener {
            dialog.dismiss()

        }




        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        dialog.show()

        //dialog.window?.setLayout(700,750)

    }



}