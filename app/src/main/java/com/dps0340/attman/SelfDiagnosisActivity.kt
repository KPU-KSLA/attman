package com.dps0340.attman

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson


class SelfDiagnosisActivity : AppCompatActivity() {
    private val englishSymptoms = arrayOf("Cough", "Fever", "Throat discomfort", "Headache", "Nasal congestion")
    private val koreanSymptoms = arrayOf("기침", "37.5도 이상 열 또는 발열감", "인후통", "두통", "코막힘")
    private val symptomsList = englishSymptoms.zip(koreanSymptoms.zip(englishSymptoms)
    { k, e ->
        "${k}(${e})"
    })
    private val flagMap = mutableMapOf<String, Boolean>()
    private val visited = mutableListOf<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selfdiagnosis_xml)
        val textView = findViewById<TextView>(R.id.tv_name5)!!
        val intent = intent
        val userName = intent.getStringExtra("userName")
        textView.text = userName
        constructFlagMap()
        inflateSymptomsLayout()
    }

    private fun constructFlagMap() {
        for(i in englishSymptoms.indices) {
            val symptom = englishSymptoms[i]
            flagMap[symptom] = false
            visited[i] = false
        }
    }


    private fun inflateSymptomsLayout() {
        val inflater = layoutInflater
        val root = findViewById<ViewGroup>(R.id.SymptomsLayout)
        for (i in symptomsList.indices) {
            inflater.inflate(R.layout.form_xml, root)
            val currentView = root.getChildAt(root.childCount - 1)
            val textView = currentView.findViewById<TextView>(R.id.symptom_textView)
            textView.text = symptomsList[i].second
            val noButton = currentView.findViewById<Button>(R.id.no_button)
            val yesButton = currentView.findViewById<Button>(R.id.yes_button)
            val noListener = ListenerFactory.makeClickListener(this, i, false, Color.GREEN, yesButton)
            val yesListener = ListenerFactory.makeClickListener(this, i, true, Color.RED, noButton)
            noListener.mutualListener = yesListener
            yesListener.mutualListener = noListener
            noButton.setOnClickListener(noListener)
            yesButton.setOnClickListener(yesListener)
        }
    }

    fun setSymptomsFlag(idx: Int, flag: Boolean) {
        if (flagMap.size <= idx) {
            return
        }
        val symptom = englishSymptoms[idx]
        visited[idx] = true
        flagMap[symptom] = flag
    }

    fun call() {
        if(visited.any{ e -> !e }) {
            return
        }
        val currentIntent = intent
        val userName = currentIntent.getStringExtra("userName")
        val userNumber = currentIntent.getStringExtra("userNumber")
        val userEmail = currentIntent.getStringExtra("userEmail")
        val userID = currentIntent.getStringExtra("userID")
        val destIntent = Intent(baseContext, ScanActivity::class.java)
        destIntent.putExtra("userName", userName)
        destIntent.putExtra("userNumber", userNumber)
        destIntent.putExtra("userID", userID)
        destIntent.putExtra("userEmail", userEmail)
        val isDangerous = flagMap.any { (k, v) -> v }
        destIntent.putExtra("dangerous?", isDangerous)
        val gson = Gson()
        destIntent.putExtra("result", gson.toJson(flagMap))
        startActivity(destIntent)
    }
}