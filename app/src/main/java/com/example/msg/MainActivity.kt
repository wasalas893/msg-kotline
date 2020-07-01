package com.example.msg

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectphoto_button_register.setOnClickListener {
            Log.d("Main","Try to the show photo selected")
            val intent=Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        register_button_register.setOnClickListener {
            performRegister()
        }
        aleady_have_account_text_edit.setOnClickListener {
            var intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

    }
    var selectedPhotoUri: Uri?=null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){

            selectedPhotoUri= data.data

            val bitmap =MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            slectphoto_imageView_register.setImageBitmap(bitmap)
            selectphoto_button_register.alpha=0f
//            val bitmapDrawable=BitmapDrawable(bitmap)
//            selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)

        }
    }
    private  fun performRegister(){
        val email=email_edittext_register.text.toString()
        val password=password_edittext_register.text.toString()

        Log.d("MainActivity","Emai is "+ email)
        Log.d("MainActivity","password is + $password")

        if(email.isEmpty()|| password.isEmpty()){
            Toast.makeText(this,"Please enter name and emai/password",Toast.LENGTH_SHORT).show()
            return
        }


        //firebase
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(!it.isSuccessful)return@addOnCompleteListener
                //else if succesfull
                Log.d("Main","Successfully created user with uid:${it.result?.user?.uid}")
                //store iamge
                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Log.d("Main","Failed to create user: ${it.message}")
                Toast.makeText(this,"Failed to create user: ${it.message}",Toast.LENGTH_SHORT).show()
            }
    }
    private fun  uploadImageToFirebaseStorage(){
     if(selectedPhotoUri==null)return
        val filename=UUID.randomUUID().toString()
        val ref=FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Main","Succesfully upload image:${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("MainActivity","File Location: $it")

                    //firebase database
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener{
                //do some logging here
            }
    }
    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        val uid=FirebaseAuth.getInstance().uid?:""
        val ref=FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user=User(uid,username_edittext_register.text.toString(),profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("MainActivity","Finally we saved user to firebase Database")

                val intent=Intent(this,LatesMessagesActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}
class User(val uid:String,val username:String,val profileImageUrl:String)
