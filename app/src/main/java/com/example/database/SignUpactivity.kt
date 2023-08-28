package com.example.database

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.database.databinding.ActivitySignUpactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpactivity : AppCompatActivity() {
private lateinit var myAuth:FirebaseAuth
private lateinit var binding: ActivitySignUpactivityBinding
private lateinit var fstore:FirebaseFirestore
private lateinit var userid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myAuth=FirebaseAuth.getInstance()
        fstore= FirebaseFirestore.getInstance()
        binding.textView.setOnClickListener {
            val intent = Intent(this, SignInactivity::class.java)
            startActivity(intent)
        }
        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassEt.text.toString()


            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    myAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {

                            userid= myAuth.currentUser!!.uid
                            val um:usermodel
                            um= usermodel(email)
                            fstore.collection("user").document(userid).set(um).addOnCompleteListener {
                                if (it.isSuccessful){
                                    Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                                }
                            }

                            val intent = Intent(this, SignInactivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }
}