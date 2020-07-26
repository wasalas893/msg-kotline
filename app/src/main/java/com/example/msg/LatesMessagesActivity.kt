package com.example.msg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_lates_messages.*
import kotlinx.android.synthetic.main.lates_message_row.view.*

class LatesMessagesActivity : AppCompatActivity() {
    companion object{
        var currentUser:User?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lates_messages)

        recycleview_lates_messages.adapter=adapter
        //setupDummyRows()
       listenForLatestMessages()

         fetchCurrentUser()

        //singout firebase method

       verifyUserIsLoogedIn()






    }
    class LatestMessageRow(val chatMessage:ChatLogActivity.ChatMessage):Item<ViewHolder>(){
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.textmessage_latet_message.text=chatMessage.text

        }
        override fun getLayout(): Int {
            return R.layout.lates_message_row
        }
    }
    val adapter=GroupAdapter<ViewHolder>()
//    private fun  setupDummyRows(){
//
//
//            adapter.add(LatestMessageRow())
//            adapter.add(LatestMessageRow())
//            adapter.add(LatestMessageRow())
//            adapter.add(LatestMessageRow())
//
//
//    }
    val latestMessagesMap=HashMap<String,ChatLogActivity.ChatMessage>()
    private fun refreshRecyclerViewMessages(){
      adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }
    private  fun listenForLatestMessages(){
        val fromId=FirebaseAuth.getInstance().uid
        val ref=FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
              val chatMessage=snapshot.getValue(ChatLogActivity.ChatMessage::class.java)?:return

                latestMessagesMap[snapshot.key!!]=chatMessage
                refreshRecyclerViewMessages()



            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage=snapshot.getValue(ChatLogActivity.ChatMessage::class.java)?:return
                latestMessagesMap[snapshot.key!!]=chatMessage
                refreshRecyclerViewMessages()

            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    private  fun fetchCurrentUser(){
        val uid=FirebaseAuth.getInstance().uid
        val ref=FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
             currentUser=snapshot.getValue(User::class.java)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private  fun verifyUserIsLoogedIn(){
        val uid=FirebaseAuth.getInstance().uid
        if(uid==null){
            val intent=Intent(this,MainActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.menu_new_message->{
                val intent=Intent(this,NewMessageActivity::class.java)
                startActivity(intent)

            }
            R.id.menu_sign_out->{
                FirebaseAuth.getInstance().signOut()
                val intent=Intent(this,MainActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

}
