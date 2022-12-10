package com.ymnberkay.test1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    //private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //auth = Firebase.auth
        val registerText: TextView = findViewById(R.id.textRegisterNow)
        registerText.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        val loginButton: Button = findViewById(R.id.buttonLogin)
        loginButton.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val email: EditText = findViewById(R.id.editTextEmailLogin)
        val password: EditText = findViewById(R.id.editTextPasswordLogin)

        if(email.text.isEmpty() || password.text.isEmpty() ) {
            Toast.makeText(this,"Please fill all the fields", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val emailInput = email.text.toString()
        val passwordInput = password.text.toString()

        /*auth.signInWithEmailAndPassword(emailInput, passwordInput)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Sign in success, navigate to the main activity
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }.addOnFailureListener(){
                Toast.makeText(baseContext, "Authentication failed. ${it.localizedMessage}",
                    Toast.LENGTH_SHORT).show()
            }*/
    }
}