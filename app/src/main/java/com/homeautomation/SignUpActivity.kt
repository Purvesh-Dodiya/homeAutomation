package com.homeautomation

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_up.edtEmail
import kotlinx.android.synthetic.main.activity_sign_up.edtPassword
import kotlinx.android.synthetic.main.activity_sign_up.btnSignUp
import kotlinx.android.synthetic.main.activity_sign_up.edtConfirmPass
import kotlinx.android.synthetic.main.activity_sign_up.progressBarView

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        progressBarView.isVisible = false
        btnSignUp.setOnClickListener {
            when {
                TextUtils.isEmpty(edtEmail.text) -> {
                    Toast.makeText(applicationContext,"Enter Email", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(edtPassword.text) -> {
                    Toast.makeText(applicationContext,"Enter Password", Toast.LENGTH_SHORT).show()
                }
                !TextUtils.equals(edtPassword.text,edtConfirmPass.text) -> {
                    Toast.makeText(applicationContext,"Password not match", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    startAuth(edtEmail.text.toString(),edtPassword.text.toString())
                }
            }
        }
    }

    private fun startAuth(email: String, password: String) {
        val auth = Firebase.auth
        btnSignUp.isVisible = false
        progressBarView.isVisible = true
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){ task ->

            if (task.isSuccessful) {
                Toast.makeText(applicationContext,"SignUp Successful",Toast.LENGTH_SHORT).show()
                auth.currentUser.sendEmailVerification().addOnCompleteListener(this){task ->
                    if(task.isSuccessful) {
                        Toast.makeText(applicationContext,"Email verification sent",Toast.LENGTH_SHORT).show()
                        auth.signOut()
                        Intent(applicationContext,LoginActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(this)
                        }
                    } else {
                        Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                        btnSignUp.isVisible = true
                        progressBarView.isVisible = false
                    }

                }

            } else {
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                btnSignUp.isVisible = true
                progressBarView.isVisible = false
            }

        }


    }
}