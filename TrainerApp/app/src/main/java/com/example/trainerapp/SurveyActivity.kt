package com.example.trainerapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.trainerapp.PerformActiviytReport.fetchDataFromFirestore
import java.util.regex.Matcher
import java.util.regex.Pattern


class SurveyActivity : AppCompatActivity() {

    private lateinit var surveyResponse: SurveyResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            collectSurveyResponses()
        }
    }

    private fun collectSurveyResponses() {
        // This function collects the responses from the UI and updates the surveyResponse object
        surveyResponse = SurveyResponse(
            primaryGoal = findViewById<RadioGroup>(R.id.radioGroupPrimaryGoal).getSelectedRadioButtonText(),
            weight = findViewById<EditText>(R.id.etWeight).text.toString().toIntOrNull(),
            height = findViewById<EditText>(R.id.etHeight).text.toString().toIntOrNull(),
            physicalBuild = findViewById<RadioGroup>(R.id.radioGroupPhysicalBuild).getSelectedRadioButtonText(),
            pastInjuries = findViewById<RadioGroup>(R.id.radioGroupPastInjuries).getSelectedRadioButtonText(), // Assuming checkboxes are inside a LinearLayout
            bodyGoal = findViewById<RadioGroup>(R.id.radioGroupBodyGoal).getSelectedRadioButtonText(),
            fitnessExperience = findViewById<RadioGroup>(R.id.radioGroupFitnessExperience).getSelectedRadioButtonText(),
            lastIdealWeight = findViewById<RadioGroup>(R.id.radioGroupLastIdealWeight).getSelectedRadioButtonText(),
            workSchedule = findViewById<RadioGroup>(R.id.radioGroupWorkSchedule).getSelectedRadioButtonText(),
            typicalDay = findViewById<RadioGroup>(R.id.radioGroupTypicalDay).getSelectedRadioButtonText()
        )

        Log.d("SurveyLog", "Primary Goal: $surveyResponse")

        var response = PerformActiviytReport.performAction(surveyResponse);

        println(response);

        val pattern: Pattern = Pattern.compile("\\bProgram (\\d+)\\b")
        val matcher: Matcher = pattern.matcher(response)
        var numberStr: String = ""
        if (matcher.find()) {
            numberStr = matcher.group(1)
            println(numberStr)
        } else {
            println("No match found.")
        }

        val result = "program$numberStr"

        fetchDataFromFirestore(object :
            FirestoreDataCallback {
            override fun onDataReceived(dataList: Map<String, Any>) {
                runOnUiThread {
                    val dataToShow:String = dataList.get(result).toString()

                    val intent = Intent(this@SurveyActivity, ResultActivity::class.java)
                    intent.putExtra(ResultActivity.RESULT_KEY, dataToShow)
                    startActivity(intent)
                }
            }

            override fun onError(e: Exception) {
                runOnUiThread {}
            }
        });
    }

    private fun RadioGroup.getSelectedRadioButtonText(): String {
        val radioButtonID = checkedRadioButtonId
        val radioButton: RadioButton = findViewById(radioButtonID)
        return radioButton.text.toString()
    }
}

data class SurveyResponse(
    var primaryGoal: String = "",
    var weight: Int? = null,
    var height: Int? = null,
    var physicalBuild: String = "",
    var pastInjuries: String = "",
    var bodyGoal: String = "",
    var fitnessExperience: String = "",
    var lastIdealWeight: String = "",
    var workSchedule: String = "",
    var typicalDay: String = ""
)



