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
    private val fullEnglishSymptoms = SYMPTOMS.englishFull
    private val symptomsList = SYMPTOMS.mapped
    internal val flagMap = mutableMapOf<String, Boolean>()
    private val visited = mutableListOf<Boolean>()
    private lateinit var caller: Caller
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selfdiagnosis_xml)
        caller = ScanActivityCaller(visited, intent, flagMap)
        val userName = intent.getStringExtra("userName")
        userName?.let {
            findViewById<TextView>(R.id.tv_name5)?.let {
                it.text = userName
            }
        }
        constructFlagMap()
        inflateSymptomsLayout()
    }

    internal fun constructFlagMap() {
        for(i in fullEnglishSymptoms.indices) {
            val symptom = fullEnglishSymptoms[i]
            flagMap[symptom] = false
            visited.add(false)
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
        val symptom = fullEnglishSymptoms[idx]
        visited[idx] = true
        flagMap[symptom] = flag
    }

    fun call() {
        caller.call(this)
    }
}