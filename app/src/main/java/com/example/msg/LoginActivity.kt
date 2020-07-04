package com.example.msg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title="User Login"

        login_button_login.setOnClickListener {
            val email=email_edit_login.text.toString()
            val password=password_edit_login.text.toString()
            //firebase
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnSuccessListener {


                    val intent= Intent(this,LatesMessagesActivity::class.java)
                    intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }


        }
        back_to_register_textview.setOnClickListener {
            finish()
        }
    }
}
