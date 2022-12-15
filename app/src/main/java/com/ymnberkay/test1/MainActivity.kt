package com.ymnberkay.test1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ymnberkay.test1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
    }
    fun signInClicked(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Enter email and password!",Toast.LENGTH_LONG).show()
        }
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener{
            val intent = Intent(this,GameScreenActivity::class.java)
            startActivity(intent)
            finish()
        }.addOnFailureListener{
            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

    fun signUpClicked(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this,"Enter email and password!",Toast.LENGTH_LONG).show()
        }
        else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                //basarili olduğunu gösteriyor
                val intent = Intent(this,FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener{
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }
    fun resetPasswordClicked(view: View){
        val intent = Intent(this,ResetPasswordActivity::class.java)
        startActivity(intent)
    }
}