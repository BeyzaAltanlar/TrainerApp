package com.example.trainerapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class ChatBotActivity : AppCompatActivity() {

    private lateinit var editTextMessage: EditText
    private lateinit var textViewResult: TextView
    private lateinit var buttonSend: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot) // replace with your actual layout file name

        editTextMessage = findViewById(R.id.editTextMessage)
        textViewResult = findViewById(R.id.textViewResult)
        buttonSend = findViewById(R.id.buttonSend)

        buttonSend.setOnClickListener {
            val message = editTextMessage.text.toString()
            // Validate the message or perform other necessary checks

            // Use coroutines for network call
            CoroutineScope(Dispatchers.IO).launch {
                val response = sendDataToFineTuning(message)
                withContext(Dispatchers.Main) {
                    textViewResult.text = response
                }
            }
        }
    }

    // You would need to handle connectivity and exceptions properly in production code
    private suspend fun sendDataToFineTuning(request: String): String? {
        val apiKey = "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" // Replace with your actual API key
        val engine = "ft:gpt-3.5-turbo-0125:personal::9GYAu1da" // Replace with your actual fine-tuned model name
        val response = StringBuilder()
        try {
            val url = URL("https://api.openai.com/v1/chat/completions")
            val conn = url.openConnection() as HttpURLConnection

            // Set up the request
            conn.setRequestMethod("POST")
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "Bearer $apiKey")
            conn.setDoOutput(true)

            // Create the JSON payload
            val jsonInputString = String.format(
                "{\"model\": \"%s\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"max_tokens\": 150}",
                engine, request
            )
            conn.outputStream.use { os ->
                val input = jsonInputString.toByteArray(charset("utf-8"))
                os.write(input, 0, input.size)
            }
            BufferedReader(
                InputStreamReader(
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) conn.inputStream else conn.errorStream,
                    "utf-8"
                )
            ).use { br ->
                var responseLine: String

                while (br.readLine().also { responseLine = it } != null) {
                    response.append(responseLine.trim { it <= ' ' })
                }
                return response.toString()
            }
        } catch (e: Exception) {
            return getResponse(response)
        }
        return ""
    }

    private suspend fun getResponse(request: StringBuilder): String? {
        val responseObj: JSONObject = JSONObject(request.toString())

        // Extract the assistant's message

        // Extract the assistant's message
        val choicesArray = responseObj.getJSONArray("choices")
        val firstChoice = choicesArray.getJSONObject(0)
        val messageObj = firstChoice.getJSONObject("message")
        val role = messageObj.getString("role")

        // Check if the role is "assistant" and then get the content

        // Check if the role is "assistant" and then get the content
        var content: String? = ""
        if ("assistant" == role) {
            content = messageObj.getString("content")
        }

        return content;
    }
}
