package com.example.quickbyte

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quickbyte.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private val binding:ActivityStartBinding by lazy{
        ActivityStartBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.loginbtn.setOnClickListener { val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)}

        binding.signupbtn.setOnClickListener { val intent= Intent(this,SignupActivity::class.java)
            startActivity(intent)}
    }
}