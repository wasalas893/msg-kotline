package com.example.msg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import androidx.recyclerview.widget.RecyclerView.ViewHolder as ViewHolder1

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title="Select User"

        //reclyview
//    val adapter=GroupAdapter<ViewHolder>()
//         adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//
//
//
//        recyclerViw_newmessage.adapter=adapter

        fetchUsers()

    }
    private fun fetchUsers(){
        val ref=FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val adapter=GroupAdapter<ViewHolder>()


              p0.children.forEach {
                  val user=it.getValue(User::class.java)
                    if(user !=null){
                        adapter.add(UserItem(user))
                    }
              }
                recyclerViw_newmessage.adapter=adapter

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}
class UserItem(val user:User):Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview_newmessage.text=user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_newmessage)

    }

    override fun getLayout(): Int {
        return  R.layout.user_row_new_message
    }
}
