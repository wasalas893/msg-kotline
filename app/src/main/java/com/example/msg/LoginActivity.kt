package com.example.msg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener {
            val email=email_edit_login.text.toString()
            val password=password_edit_login.text.toString()

        }
        back_to_register_textview.setOnClickListener {
            finish()
        }
    }
}
