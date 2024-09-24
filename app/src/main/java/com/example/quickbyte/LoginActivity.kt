package com.example.quickbyte

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quickbyte.databinding.ActivityLoginBinding
import com.example.quickbyte.model.UserModal
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var email:String
    private lateinit var password:String
    private  var userName:  String?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val binding:ActivityLoginBinding by lazy{
     ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        //initialise firebase auth
        auth= Firebase.auth
        //initialise firebase database
        database= Firebase.database.reference

        binding.LoginButton.setOnClickListener {
            //get text from edit text
            email=binding.email.text.toString().trim()
            password=binding.password.text.toString().trim()

            if(email.isBlank()||password.isBlank()){
                Toast.makeText(this,"Please Fill All The Details",Toast.LENGTH_SHORT).show()
            }
            else{
                createuser(email,password)
            }

        }
        binding.donthavebutton.setOnClickListener {
            val intent= Intent(this,SignupActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createuser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                updateUi(user)
            } else {
                Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { createTask ->
                    if (createTask.isSuccessful) {
                        val user = auth.currentUser
                        saveUserData()
                        updateUi(user)
                    } else {
                        Toast.makeText(this, "Sign up failed: ${createTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun saveUserData() {
        //get text from edit text
        email=binding.email.text.toString().trim()
        password=binding.password.text.toString().trim()

        val user=UserModal(userName,email,password)
        val userId=FirebaseAuth.getInstance().currentUser?.uid
        userId?.let{
            database.child("user ").child(it).setValue(user)
        }
    }

    private fun updateUi(user: FirebaseUser?){
        startActivity(Intent(this,MainActivity::class.java))
        finish()

    }
}