package com.geelong.user.Adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.geelong.user.API.APIUtils
import com.geelong.user.Activity.Review
import com.geelong.user.R
import com.geelong.user.Response.DataXX
import com.geelong.user.Response.LoginResponse
import com.geelong.user.Response.TripHistoryData
import com.geelong.user.ReveiwResponse
import com.geelong.user.Util.ConstantUtils
import com.geelong.user.Util.SharedPreferenceUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_acccount.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class TripAdapter(var mContext: Context,var mlist: List<TripHistoryData>) : RecyclerView.Adapter<TripAdapter.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {


        lateinit var user_image:ImageView
        lateinit var user_name:TextView
        lateinit var user_distance_travel:TextView
        lateinit var user_fare:TextView
        lateinit var user_trip_time:TextView
        lateinit var user_pick_up_pick:TextView
        lateinit var user_drop_location:TextView
        lateinit var wr_a_reveiw:TextView
        var booking_id:String=""
        var user_idd:String=""
        var driver_id_id:String=""



        init {

            user_image=itemView.findViewById(R.id.user_image_trip)
            user_name=itemView.findViewById(R.id.user_profile_trip)
            user_distance_travel=itemView.findViewById(R.id.user_distance_travel_trip)
            user_fare=itemView.findViewById(R.id.user_price_trip)
            user_trip_time=itemView.findViewById(R.id.user_time_trip)
            user_pick_up_pick=itemView.findViewById(R.id.user_pickup_trip)
            user_drop_location=itemView.findViewById(R.id.user_drop_trip)
            wr_a_reveiw=itemView.findViewById(R.id.write_a_Reveiw)


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
      holder.booking_id=mlist[position].id
        holder.driver_id_id=mlist[position].driver_id
        holder.user_idd=mlist[position].uid


  var img_url=mlist[position].driver_photo
        holder.user_name.text=mlist[position].driver_name
        holder.user_distance_travel.text=mlist[position].distance
        holder.user_fare.text="₹"+mlist[position].amount
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

        holder.wr_a_reveiw.setOnClickListener {
          showDialog(holder.booking_id,holder.driver_id_id,holder.user_idd)

        }
        holder.itemView.setOnClickListener()
        {
            /*var Descriptionn=notiData[position].description

            var intent= Intent(mContext,Help_des::class.java)
            intent.putExtra("Desc",Descriptionn)
            mContext.startActivity(intent)*/
            Toast.makeText(mContext,position.toString(),Toast.LENGTH_LONG).show()


        }


    }


    fun showDialog(booking_idd:String,driver_id_idd:String,user_iddd:String) {
        val dialog = BottomSheetDialog(mContext)

      //  val dialog = Dialog(mContext,android.R.style.ThemeOverlay_Material_Light)

        dialog.getWindow()!!
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);



        dialog.setCancelable(true)
        dialog.setContentView(R.layout.popup_write_review)

       /* var booking_id=SharedPreferenceUtils.getInstance(mContext)?.getStringValue(ConstantUtils.Booking_id,"").toString()
        var driver_id=SharedPreferenceUtils.getInstance(mContext)?.getStringValue(ConstantUtils.Driver_Id,"").toString()
        var user_id=SharedPreferenceUtils.getInstance(mContext)?.getStringValue(ConstantUtils.USER_ID,"").toString()*/


        var reveiw_edittext=dialog.findViewById<EditText>(R.id.edit_review)
        var rating_bar=dialog.findViewById<RatingBar>(R.id.rBar)

        var submit_review_btn=dialog.findViewById<TextView>(R.id.submit_review_btn)

           submit_review_btn?.setOnClickListener {
               val reveiw_string_value=reveiw_edittext?.text.toString()
                val rating_val= rating_bar?.rating.toString()

               if (reveiw_string_value.isEmpty())
               {
                  Toast.makeText(mContext,"Please Write Review",Toast.LENGTH_LONG).show()
               }
               else if (rating_val.equals("0.0"))
               {
                   Toast.makeText(mContext,"Please rate your Review",Toast.LENGTH_LONG).show()
               }

               else
               {
                  writereveiw(rating_val,reveiw_string_value,booking_idd,driver_id_idd,user_iddd)
                   dialog.hide()

               }

           }




        dialog.show()

    }

    override fun getItemCount(): Int {

        return mlist.size
    }



    fun writereveiw(reveiw_string:String,driver_rating:String,booking_id:String,user_id:String,driver_id:String)
    {

        val request = HashMap<String, String>()
        request.put("booking_id",booking_id)
        request.put("user_id",user_id)
        request.put("driver_id",driver_id)
        request.put("rating",reveiw_string)
        request.put("review",driver_rating)





        var login_in: Call<ReveiwResponse> = APIUtils.getServiceAPI()!!.Review(request)

        login_in.enqueue(object : Callback<ReveiwResponse> {
            override fun onResponse(call: Call<ReveiwResponse>, response: Response<ReveiwResponse>) {
                try {


                    if (response.body()!!.success.equals("true")) {
                        Toast.makeText(mContext,response.body()!!.msg.toString(), Toast.LENGTH_LONG).show()




                    } else {

                        Toast.makeText(mContext,"Error", Toast.LENGTH_LONG).show()

                    }

                }  catch (e: Exception) {
                    Log.e("saurav", e.toString())
                    Toast.makeText(mContext,e.message, Toast.LENGTH_LONG).show()


                }

            }

            override fun onFailure(call: Call<ReveiwResponse>, t: Throwable) {
                Log.e("Saurav", t.message.toString())
                Toast.makeText(mContext,t.message, Toast.LENGTH_LONG).show()

            }

        })
    }

}
