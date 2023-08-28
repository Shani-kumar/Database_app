package com.example.database

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.database.databinding.ActivitySignInactivityBinding
import com.google.firebase.auth.FirebaseAuth

class SignInactivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignInactivityBinding
    private lateinit var myAuth:FirebaseAuth
    private lateinit var useremail:String
    private lateinit var userpassword:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignInactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myAuth=FirebaseAuth.getInstance()

        binding.textView.setOnClickListener{
            val intent= Intent(this,SignUpactivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            useremail=binding.emailLayouttext.text.toString()
            userpassword=binding.passwordLayouttext.text.toString()
            if(useremail.isBlank()){
                binding.emailLayout.error="Email can't be Blank"
            }
            if(userpassword.isEmpty()){
                binding.passwordLayout.error="Password can't be empty"
            }
            if(Checkformdetails(useremail,userpassword)){

                myAuth.signInWithEmailAndPassword(useremail, userpassword).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("Email",useremail)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
    private fun Checkformdetails(useremail:String,userpassword:String): Boolean {

        return !(useremail.isBlank() || userpassword.isEmpty())
    }
}