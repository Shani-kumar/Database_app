package com.example.database

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.database.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var myAuth: FirebaseAuth
    private lateinit var fstore:FirebaseFirestore
    private lateinit var binding: ActivityMainBinding
    private lateinit var userid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref=this?.getPreferences(Context.MODE_PRIVATE)?:return
        val isLogin=sharedPref.getString("Email","1")

        if(isLogin=="1") {
            val email=intent.getStringExtra("Email")
            if(email!=null){
                setText()
                with(sharedPref.edit()) {
                    putString("Email", email)
                    apply()
                }
            }
            else{
                var intent=Intent(this,SignInactivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        else
        {
            setText()
        }

    }
    private fun setText(){
        myAuth=FirebaseAuth.getInstance()
        fstore= FirebaseFirestore.getInstance()
        val currentUser = myAuth.currentUser
        if (currentUser != null) {
            userid = currentUser.uid

            val doc=fstore.collection("user").document(userid)
            doc.addSnapshotListener { snapshot, e ->
                if(e!=null){
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }
                if ((snapshot != null) && snapshot.exists()) {
                    binding.outputtext.text=snapshot.getString("email")
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
            binding.btn.setOnClickListener {
                myAuth.signOut()
                val intent = Intent(this, SignInactivity::class.java)
                startActivity(intent)
                finish()
            }
            // Continue with the rest of the code that depends on `userid`.
        } else {
            // Handle the case when the user is not authenticated or the authentication state is not resolved yet.
            // For example, you can redirect the user to the sign-in activity.
            var intent=Intent(this,SignInactivity::class.java)
            startActivity(intent)
            finish()
        }
//        userid=myAuth.currentUser!!.uid

    }

}