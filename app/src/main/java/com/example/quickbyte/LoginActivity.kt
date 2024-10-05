package com.example.quickbyte

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quickbyte.databinding.ActivityLoginBinding
import com.example.quickbyte.model.UserModal
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.GoogleSignatureVerifier
import com.google.android.gms.signin.internal.SignInClientImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private lateinit var email:String
    private lateinit var password:String
    private  var userName:  String?=null
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googlesignInClient: GoogleSignInClient
    private val binding:ActivityLoginBinding by lazy{
     ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val googleSignInOptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
           .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        //initialise firebase auth
        auth= Firebase.auth
        //initialise firebase database
        database= Firebase.database.reference
        //initistlise google sign in
       googlesignInClient= GoogleSignIn.getClient(this,googleSignInOptions)

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
        binding.googlebtn.setOnClickListener {
            val signIntent=googlesignInClient.signInIntent
            launcher.launch(signIntent)
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


    // launcher pop up for google sign in
    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if(result.resultCode== Activity.RESULT_OK){
            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account:GoogleSignInAccount=task.result
                val credentials=GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credentials).addOnCompleteListener { authTask->
                    if(authTask.isSuccessful){
                        Toast.makeText(this,"Successfully LogIn With Google",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Google Sign Failed",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    //check if user is already logged in
    //override fun onStart(){
     //   super.onStart()
   //     val currentUuser=auth.currentUser
     //   if(currentUuser!=null){
       //     startActivity(Intent(this,MainActivity::class.java))
      //      finish()
     //   }
   // }
    //function to sent us to main activicty
    private fun updateUi(user: FirebaseUser?){
        startActivity(Intent(this,MainActivity::class.java))
        finish()

    }
}
