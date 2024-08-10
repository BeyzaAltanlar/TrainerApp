package com.example.trainerapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // UI elements
    private lateinit var tvForgotPassword: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnCreateAccount: Button
    private lateinit var mProgressBar: ProgressBar
    // Firebase references
//    private lateinit var mAuth: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        initialise()
    }

    private fun initialise() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        }

        // Initialize UI elements
        tvForgotPassword = findViewById(R.id.tv_forgot_password)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        btnCreateAccount = findViewById(R.id.btn_register_account)
        mProgressBar = findViewById(R.id.progressBar) // Assuming you have a ProgressBar with this ID

//        mAuth = FirebaseAuth.getInstance()

        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }
        btnLogin.setOnClickListener { loginUser() }
    }


    private fun loginUser() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            startActivity(Intent(this, SurveyActivity::class.java))
            finish()
//            mProgressBar.visibility = ProgressBar.VISIBLE
//            mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this) { task ->
//                    mProgressBar.visibility = ProgressBar.INVISIBLE
//                    if (task.isSuccessful) {
//                        startActivity(Intent(this, SurveyActivity::class.java))
//                        finish()
//                    } else {
//                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
//                    }
//                }
        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }
}
