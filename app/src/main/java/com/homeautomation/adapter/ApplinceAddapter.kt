package com.homeautomation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.angads25.toggle.interfaces.OnToggledListener
import com.github.angads25.toggle.model.ToggleableView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.homeautomation.R
import kotlinx.android.synthetic.main.iteam_recycler.view.imageView
import kotlinx.android.synthetic.main.iteam_recycler.view.switchToggle

class ApplinceAddapter(list: ArrayList<Model>,contaxt: Context) : RecyclerView.Adapter<ApplinceAddapter.ViewHolder>() {
    val dataList = list
    val contextNew = contaxt
    private lateinit var database: DatabaseReference
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val imageIcon: ImageView = view.imageView
        val switch = view.switchToggle
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ApplinceAddapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.iteam_recycler,parent,false)
        return ViewHolder(view)
    }


    override fun getItemCount() : Int = dataList.count()

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {
        Glide.with(contextNew).load(dataList[position].img).placeholder(R.drawable.ic_hall).into(holder.imageIcon)
        holder.switch.isOn = dataList[position].status.equals("ON")

        holder.switch.setOnToggledListener(object : OnToggledListener{
            override fun onSwitched(toggleableView : ToggleableView?, isOn : Boolean) {
                val userID = Firebase.auth.uid.toString()
                database = Firebase.database.reference.child("users").child(userID).child("SWITCH").child(dataList[position].primises).child(dataList[position].key).child("status")

               if (toggleableView?.isOn == false) {
                   database.setValue("OFF")
               } else {
                   database.setValue("ON")
               }
            }
        })
    }
}