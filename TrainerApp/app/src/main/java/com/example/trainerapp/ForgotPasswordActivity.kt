package com.example.trainerapp


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var btnSubmit: Button
//    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        initialise()
    }

    private fun initialise() {
        etEmail = findViewById(R.id.et_email)
        btnSubmit = findViewById(R.id.btn_submit)
//        mAuth = FirebaseAuth.getInstance()

        btnSubmit.setOnClickListener { sendPasswordResetEmail() }
    }

    private fun sendPasswordResetEmail() {
        val email = etEmail.text.toString().trim()

        if (email.isNotEmpty()) {
//            mAuth.sendPasswordResetEmail(email)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.d(Tag, "Email sent.")
//                        Toast.makeText(this, "Email sent.", Toast.LENGTH_SHORT).show()
//                        startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
//                        finish() // Close the activity to prevent returning to it.
//                    } else {
//                        task.exception?.let {
//                            Log.w(Tag, it.message ?: "Error occurred during password reset.")
//                            Toast.makeText(this, it.localizedMessage ?: "Error occurred during password reset.", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
        } else {
//            Toast.makeText(this, "Enter your email address.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val Tag = "ForgotPasswordActivity"
    }
}
