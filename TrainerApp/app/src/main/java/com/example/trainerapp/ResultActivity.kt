package com.example.trainerapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val dataToShow = intent.getStringExtra(RESULT_KEY)

        val textViewResult = findViewById<TextView>(R.id.textViewResult)

        textViewResult.text = dataToShow ?: "No data received"

        val buttonSeeProgram: Button = findViewById(R.id.buttonSeeProgram)
        buttonSeeProgram.setOnClickListener {
            PerformActiviytReport.fetchDataFromFirestoreFitness(object :
                FirestoreDataCallback {
                override fun onDataReceived(dataList: Map<String, Any>) {
                    runOnUiThread {
                        val sportProgram: String = dataList.get(dataToShow).toString()

                        val intent = Intent(this@ResultActivity, SporActivity::class.java)
                        intent.putExtra(RESULT_KEY, sportProgram)
                        startActivity(intent)
                    }
                }

                override fun onError(e: Exception) {
                    runOnUiThread {}
                }
            });
        }

        val buttonAskChatbot: Button = findViewById(R.id.buttonAskChatbot)
        buttonAskChatbot.setOnClickListener {
            startActivity(Intent(this, ChatBotActivity::class.java))
        }
    }

    companion object {
        const val RESULT_KEY = "result_key"
    }
}
