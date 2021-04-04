package com.homeautomation

import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add.btnAdd
import kotlinx.android.synthetic.main.activity_add.edtName
import kotlinx.android.synthetic.main.activity_add.progressBarLoading
import kotlinx.android.synthetic.main.activity_add.spinnerApplinces
import kotlinx.android.synthetic.main.activity_add.spinnerPrimices

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setUp()


        btnAdd.setOnClickListener{
            when {
                TextUtils.isEmpty(edtName.text) -> {
                    Toast.makeText(applicationContext, "Please enter name of appliances", Toast.LENGTH_SHORT).show()
                }
                spinnerPrimices.selectedItemPosition == 0 -> {
                    Toast.makeText(applicationContext, "Please select where you you want to add", Toast.LENGTH_SHORT).show()
                }
                spinnerApplinces.selectedItemPosition == 0 -> {
                    Toast.makeText(applicationContext, "Please select appliances", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    addToDatabase()
                }
            }
        }
    }

    private fun addToDatabase() {
        progressBarLoading.isVisible = true
        btnAdd.isVisible = false
        val map: MutableMap<String, Any> = HashMap()
        map.apply {
            val ref = Firebase.database.reference.push()
            put("name", edtName.text.toString())
            put("status", "OFF")
            put("key", spinnerPrimices.selectedItem.toString() + "_" + spinnerApplinces.selectedItem.toString() + "_" + ref)
            when(spinnerApplinces.selectedItem.toString()) {
                "AC" -> {
                    put("img", resources.getString(R.string.acImg))
                }
                "TV" -> {
                    put("img", resources.getString(R.string.tvImg))
                }
                "FAN" -> {
                    put("img", resources.getString(R.string.fanImg))
                }
                "LIGHT" -> {
                    put("img", resources.getString(R.string.lightImg))
                }
                else -> {
                    put("img", resources.getString(R.string.lightImg))
                }
            }
        }

        val userID = Firebase.auth.uid.toString()
        val  database = Firebase.database.reference.child("users").child(userID).child("SWITCH").child(spinnerPrimices.selectedItem.toString()).child(spinnerApplinces.selectedItem.toString())
        database.updateChildren(map).addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                Toast.makeText(applicationContext,"Added Successfully",Toast.LENGTH_SHORT).show()
                finish()
            }else {
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                progressBarLoading.isVisible = false
                btnAdd.isVisible = true
            }

        }
    }

    private fun setUp() {
        progressBarLoading.isVisible = false
        ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.appliancesArray)).apply {
            spinnerApplinces.adapter = this
        }
        ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.primicesArray)).apply {
            spinnerPrimices.adapter = this
        }
    }
}