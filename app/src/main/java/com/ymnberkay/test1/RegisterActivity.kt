package com.ymnberkay.test1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        val loginText: TextView = findViewById(R.id.textLoginNow)
        loginText.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerButton: Button = findViewById(R.id.buttonRegister)

        registerButton.setOnClickListener{
            performSignUp()
        }


    }

    private fun performSignUp() {
        val email = findViewById<EditText>(R.id.editTextEmailRegister)
        val password = findViewById<EditText>(R.id.editTextPasswordRegister)

        if (email.text.isEmpty() || password.text.isEmpty()) {
            Toast.makeText(this,"Please fill all fields", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val inputEmail = email.toString()
        val inputPassword = password.toString()

        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    print("Basarili")
                    // Sign in success, let move to the next activity
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)

                    Toast.makeText(baseContext, "Basarili.",
                        Toast.LENGTH_SHORT).show()

                } else {
                    // If sign in fails, display a message to the user
                    print("Basarisiz")
                    Toast.makeText(baseContext, "Basarisiz",
                        Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Error occured ${it.localizedMessage}", Toast.LENGTH_SHORT)
                    .show()
            }
    }


}