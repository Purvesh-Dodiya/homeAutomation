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
import kotlinx.android.synthetic.main.activity_login.createAccount
import kotlinx.android.synthetic.main.activity_login.edtEmail
import kotlinx.android.synthetic.main.activity_login.edtPassword
import kotlinx.android.synthetic.main.activity_login.progressBar
import kotlinx.android.synthetic.main.activity_sign_up.btnSignUp

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressBar.visibility = View.GONE

        btnLogin.setOnClickListener(){
            when {
                TextUtils.isEmpty(edtEmail.text) -> {
                    Toast.makeText(applicationContext,"Enter Email",Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(edtPassword.text) -> {
                    Toast.makeText(applicationContext,"Enter Password",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    startAuth()
                }
            }
        }

        createAccount.setOnClickListener() {
            Intent(applicationContext,SignUpActivity::class.java).apply {
                startActivity(this)
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

                        if (auth.currentUser.isEmailVerified) {
                            Intent(applicationContext,MainActivity::class.java).apply {
                                startActivity(this)
                            }
                        } else {
                            auth.signOut()
                            Toast.makeText(applicationContext,"Please verify email",Toast.LENGTH_SHORT).show()
                            progressBar.visibility = View.GONE
                            btnLogin.visibility = View.VISIBLE
                        }

                    } else {
                        progressBar.visibility = View.GONE
                        btnLogin.visibility = View.VISIBLE
                        Toast.makeText(applicationContext,task.exception?.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                    }
                }

    }
}