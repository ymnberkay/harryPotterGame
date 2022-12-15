package com.ymnberkay.test1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.ymnberkay.test1.databinding.ActivityMainBinding
import com.ymnberkay.test1.databinding.ActivityResetPasswordBinding

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityResetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
    fun rPassword(){
        val email = binding.editTextEmail.text.toString()
        if(email.isEmpty()){
            Toast.makeText(this,"Enter email", Toast.LENGTH_LONG).show()
        }
        else{
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnSuccessListener {
                Toast.makeText(this,"Basarili..", Toast.LENGTH_LONG).show()
                finish()
            }.addOnFailureListener{
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }


        }
    }
}