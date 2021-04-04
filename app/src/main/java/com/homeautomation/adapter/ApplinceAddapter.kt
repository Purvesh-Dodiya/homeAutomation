package com.homeautomation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.homeautomation.R
import kotlinx.android.synthetic.main.iteam_recycler.view.imageView
import kotlinx.android.synthetic.main.iteam_recycler.view.switchToggle
import kotlinx.android.synthetic.main.iteam_recycler.view.txtLabel

class ApplinceAddapter(list : ArrayList<Model>, contaxt : Context) : RecyclerView.Adapter<ApplinceAddapter.ViewHolder>() {
    val dataList = list
    val contextNew = contaxt
    private lateinit var database: DatabaseReference
    class ViewHolder(view : View): RecyclerView.ViewHolder(view) {
        val imageIcon: ImageView = view.imageView
        val switch = view.switchToggle
        val txtLabel = view.txtLabel

    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : ApplinceAddapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.iteam_recycler, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount() : Int = dataList.count()

    override fun onBindViewHolder(holder : ViewHolder, position : Int) {

        holder.switch.isOn = dataList[position].status.equals("ON")
        holder.txtLabel.text = dataList[position].name

        holder.switch.colorOn = ContextCompat.getColor(contextNew, R.color.switchColor)
        holder.switch.colorBorder = ContextCompat.getColor(contextNew,R.color.switchColor)

        //Initial SetUp
        if (dataList[position].key.contains("FAN")) {
            Glide.with(contextNew).load(dataList[position].img).placeholder(R.drawable.placeholder).into(holder.imageIcon)

            if (dataList[position].status == "ON"){
                fanAnimationStart(holder.imageIcon)
            }else {
                fanAnimationStop(holder.imageIcon)
            }

        } else if (dataList[position].key.contains("LIGHT")) {
            if (dataList[position].status == "ON"){
                bulbAnimationStart(holder.imageIcon)
            }else {
                bulbAnimationStop(holder.imageIcon)
            }
        } else if (dataList[position].key.contains("AC")) {
            if (dataList[position].status == "ON"){
                acAnimationStart(holder.imageIcon)
            }else {
                acAnimationStop(holder.imageIcon)
            }
        } else if (dataList[position].key.contains("TV")) {
            if (dataList[position].status == "ON"){
                tvAnimationStart(holder.imageIcon)
            }else {
                tvAnimationStop(holder.imageIcon)
            }
        } else {
            Glide.with(contextNew).load(dataList[position].img).placeholder(R.drawable.placeholder).into(holder.imageIcon)
        }

        holder.switch.setOnToggledListener { toggleableView, isOn ->
            val userID = Firebase.auth.uid.toString()
            database = Firebase.database.reference.child("users").child(userID).child("SWITCH").child(dataList[position].primises).child(dataList[position].key).child("status")

            if (toggleableView?.isOn == false) {
                database.setValue("OFF")
                when {
                    dataList[position].key.equals("FAN") -> {
                        fanAnimationStop(holder.imageIcon)
                    }
                    dataList[position].key.equals("LIGHT") -> {
                        bulbAnimationStop(holder.imageIcon)
                    }
                    dataList[position].key.equals("TV") -> {
                        tvAnimationStop(holder.imageIcon)
                    }
                    dataList[position].key.equals("AC") -> {
                        acAnimationStop(holder.imageIcon)
                    }
                }
            } else {
                database.setValue("ON")
                when {
                    dataList[position].key.equals("FAN") -> {
                        fanAnimationStart(holder.imageIcon)
                    }
                    dataList[position].key.equals("LIGHT") -> {
                        bulbAnimationStart(holder.imageIcon)
                    }
                    dataList[position].key.equals("AC") -> {
                        acAnimationStart(holder.imageIcon)
                    }
                    dataList[position].key.equals("TV") -> {
                        tvAnimationStart(holder.imageIcon)
                    }
                }
            }
        }
    }

    fun fanAnimationStart(image: ImageView){
        val rotate = RotateAnimation(0F, 360F, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.interpolator = LinearInterpolator()
        rotate.repeatMode = Animation.RESTART
        rotate.repeatCount = Animation.INFINITE
        rotate.duration = 1000
        image.startAnimation(rotate)
    }

    fun fanAnimationStop (image: ImageView){
        image.clearAnimation()
    }

    fun bulbAnimationStart(image: ImageView) {
        image.setImageResource(R.drawable.bulb_on)
        val rotate =AlphaAnimation(0.2F,1F)
        rotate.interpolator = LinearInterpolator()
        rotate.repeatMode = Animation.REVERSE
        rotate.repeatCount = Animation.INFINITE
        rotate.duration = 500

        image.startAnimation(rotate)
    }
    fun bulbAnimationStop(image: ImageView){
        image.setImageResource(R.drawable.bulb_off)
        image.clearAnimation()
    }

    fun acAnimationStart(image: ImageView) {
        image.setImageResource(R.drawable.ac_on)
        image.scaleX = 0.7F
        image.scaleY = 0.7F
        val rotate =RotateAnimation(-20F, 20F, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.interpolator = LinearInterpolator()
        rotate.repeatMode = Animation.REVERSE
        rotate.repeatCount = Animation.INFINITE
        rotate.duration = 500
        image.startAnimation(rotate)
    }
    fun acAnimationStop(image: ImageView){
        image.setImageResource(R.drawable.air_conditioner_off)
        image.scaleX = 1F
        image.scaleY = 1F
        image.clearAnimation()
    }

    fun tvAnimationStart(image: ImageView) {
        image.setImageResource(R.drawable.tv_screen_on)
        val rotate = ScaleAnimation(0.5F,1F,0.5F,1F, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotate.interpolator = LinearInterpolator()
        rotate.repeatMode = Animation.REVERSE
        rotate.repeatCount = Animation.INFINITE
        rotate.duration = 500
        image.startAnimation(rotate)
    }
    fun tvAnimationStop(image: ImageView){
        image.setImageResource(R.drawable.tv_screen_off)
        image.clearAnimation()
    }
}