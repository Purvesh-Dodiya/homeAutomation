package com.homeautomation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.btnLogin
import kotlinx.android.synthetic.main.activity_login.edtEmail
import kotlinx.android.synthetic.main.activity_login.edtPassword
import kotlinx.android.synthetic.main.activity_login.progressBar

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressBar.visibility = View.GONE

        btnLogin.setOnClickListener(){
            if ( TextUtils.isEmpty(edtEmail.text) ) {
                Toast.makeText(applicationContext,"Enter Email",Toast.LENGTH_SHORT).show()
            } else if  ( TextUtils.isEmpty(edtPassword.text)) {
                Toast.makeText(applicationContext,"Enter Password",Toast.LENGTH_SHORT).show()
            } else {
                startAuth()
            }
        }
    }

    private fun startAuth() {
        progressBar.visibility = View.VISIBLE
        btnLogin.visibility = View.INVISIBLE
        var auth = Firebase.auth
        auth.signInWithEmailAndPassword(edtEmail.text.toString(), edtPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Intent(applicationContext,MainActivity::class.java).apply {
                            startActivity(this)
                        }
                    } else {
                        progressBar.visibility = View.GONE
                        btnLogin.visibility = View.VISIBLE
                        Toast.makeText(applicationContext,task.exception?.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                    }
                }

    }
}