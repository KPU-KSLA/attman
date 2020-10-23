package com.dps0340.attman

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.google.gson.Gson
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import org.jetbrains.anko.toast
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class SelfDiagnosisActivity : AppCompatActivity() {
    private val englishSymptoms = arrayOf("Cough", "Fever", "Throat discomfort", "Headache", "Nasal congestion")
    private val koreanSymptoms = arrayOf("기침", "37.5도 이상 열 또는 발열감", "인후통", "두통", "코막힘")
    private val symptomsList = englishSymptoms.zip(koreanSymptoms.zip(englishSymptoms)
    { k, e ->
        "${k}(${e})"
    })
    private val flags = (symptomsList.indices).map {
        0
    }.toMutableList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selfdiagnosis_xml)
        val textView = findViewById<TextView>(R.id.tv_name5)!!
        val intent = intent
        val userName = intent.getStringExtra("userName")
        textView.text = userName
        inflateSymptomsLayout()
    }


    private fun inflateSymptomsLayout() {
        val size = symptomsList.size
        val inflater = layoutInflater
        val root = findViewById<ViewGroup>(R.id.SymptomsLayout)
        for (i in 0 until size) {
            inflater.inflate(R.layout.form_xml, root)
            val currentView = root.getChildAt(root.childCount - 1)
            val textView = currentView.findViewById<TextView>(R.id.symptom_textView)
            textView.text = symptomsList[i].second
            val noButton = currentView.findViewById<Button>(R.id.no_button)
            val yesButton = currentView.findViewById<Button>(R.id.yes_button)
            noButton.setOnClickListener(ListenerFactory.makeClickListener(this, i, 2, Color.GREEN))
            yesButton.setOnClickListener(ListenerFactory.makeClickListener(this, i, 1, Color.RED))
        }
    }

    fun setSymptomsFlag(idx: Int, flag: Int) {
        if (flags.size <= idx) {
            return
        }
        flags[idx] = flag
    }







    fun call() {
        if(flags.any{ e -> e == 0 }) {
            return
        }
        val currentIntent = intent
        val userName = currentIntent.getStringExtra("userName")
        val userNumber = currentIntent.getStringExtra("userNumber")
        val userEmail = currentIntent.getStringExtra("userEmail")
        val userID = currentIntent.getStringExtra("userID")
        val destIntent = Intent(baseContext, HomeActivity::class.java)
        for (i in symptomsList.indices) {
            val p = symptomsList[i]
            val name = p.first
            val flag = flags[i]
            destIntent.putExtra(name, flag)
        }
        destIntent.putExtra("userName", userName)
        destIntent.putExtra("userNumber", userNumber)
        destIntent.putExtra("userID", userID)
        destIntent.putExtra("userEmail", userEmail)
        val isDangerous = flags.any { e -> e == 1 }
        destIntent.putExtra("dangerous?", isDangerous)
        val gson = Gson()
        destIntent.putExtra("result", gson.toJson(flags))
        startActivity(destIntent)
    }
}