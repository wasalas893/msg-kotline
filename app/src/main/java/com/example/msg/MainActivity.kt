package com.example.msg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        register_button_register.setOnClickListener {
            val email=email_edittext_register.text.toString()
            val password=password_edittext_register.text.toString()

            Log.d("MainActivity","Emai is "+ email)
            Log.d("MainActivity","password is + $password")

            //firebase
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    if(!it.isSuccessful)return@addOnCompleteListener
                    //else if succesfull
                    Log.d("Main","Successfully created user with uid:${it.result?.user?.uid}")
                }
        }
        aleady_have_account_text_edit.setOnClickListener {
            var intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
