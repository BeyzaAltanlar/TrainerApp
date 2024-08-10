package com.example.trainerapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnCreateAccount: Button
//    private lateinit var progressBar: CircularProgressIndicator

//    private lateinit var mAuth: FirebaseAuth
    private val TAG = "CreateAccountActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        initialise()
    }

    private fun initialise() {
        etFirstName = findViewById(R.id.et_first_name)
        etLastName = findViewById(R.id.et_last_name)
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnCreateAccount = findViewById(R.id.btn_register)
//        progressBar = findViewById(R.id.progress_circular)

        // Get instances of FirebaseAuth and FirebaseDatabase
//        mAuth = FirebaseAuth.getInstance()
//        val mDatabase = FirebaseDatabase.getInstance().reference.child("Users")

        btnCreateAccount.setOnClickListener { createNewAccount() }
    }

    private fun createNewAccount() {
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (validateForm(firstName, lastName, email, password)) {
//            progressBar.show() // This would show a loading indicator to the user.

//            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
//                progressBar.hide() // Hide the loading indicator.

//                if (task.isSuccessful) {
                    Log.d(TAG, "CreateUserWithEmail:Success")
//                    val userId = mAuth.currentUser!!.uid
                    // Verify the email
                    verifyEmail()
                    // Update user information in the database
//                    databaseReference.child(userId).setValue(User(firstName, lastName))
                    updateUserInfoAndUI()
//                } else {
//                    Log.w(TAG, "CreateUserWithEmail:Failure", task.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
//                }
//            }
        } else {
            Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateForm(firstName: String, lastName: String, email: String, password: String): Boolean {
        return firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
    }

    private fun updateUserInfoAndUI() {
        val intent = Intent(this, SurveyActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun verifyEmail() {
//        mAuth.currentUser?.let { user ->
//            user.sendEmailVerification().addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(this, "Verification email sent to ${user.email}.", Toast.LENGTH_SHORT).show()
//                } else {
//                    Log.e(TAG, "SendEmailVerification", task.exception)
//                    Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
    }

    data class User(val firstName: String, val lastName: String)
}