package com.homeautomation.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.homeautomation.R
import com.homeautomation.adapter.ApplinceAddapter
import com.homeautomation.adapter.Model
import kotlinx.android.synthetic.main.fragment_hall_frament.recyclerHall

class HallFrament : Fragment() {
    var contextNew: Context? = null

    var listData: ArrayList<Model> = arrayListOf()
    private lateinit var database: DatabaseReference
    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?,
                              savedInstanceState : Bundle?) : View? {
        contextNew = container?.context
        return inflater.inflate(R.layout.fragment_hall_frament, container, false)
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData(view.context)
        recyclerHall.layoutManager = LinearLayoutManager(context)




    }

    private fun loadData(context: Context) {

        val userID = Firebase.auth.uid.toString()
        database = Firebase.database.reference.child("users").child(userID).child("SWITCH/HALL")

        database.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot : DataSnapshot, previousChildName : String?) {

                var a = snapshot.getValue<Model>()
                a?.key  = snapshot.key.toString()
                a?.primises = "HALL"
                a?.let { listData.add(it) }
                ApplinceAddapter(listData,context).apply {
                    recyclerHall.adapter = this

                }
            }

            override fun onChildChanged(snapshot : DataSnapshot, previousChildName : String?) {
            }

            override fun onChildRemoved(snapshot : DataSnapshot) {
            }

            override fun onChildMoved(snapshot : DataSnapshot, previousChildName : String?) {
            }

            override fun onCancelled(error : DatabaseError) {
            }
        })
    }
}