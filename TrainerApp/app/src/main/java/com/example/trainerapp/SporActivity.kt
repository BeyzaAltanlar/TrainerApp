package com.example.trainerapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SporActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport)
        val dataToShow = intent.getStringExtra(com.example.trainerapp.ResultActivity.RESULT_KEY)

        val textViewResult = findViewById<TextView>(R.id.textViewResult)

        textViewResult.text = dataToShow ?: "No data received"
    }

    companion object {
        const val RESULT_KEY = "result_key"
    }
}