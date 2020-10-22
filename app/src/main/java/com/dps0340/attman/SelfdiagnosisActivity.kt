package com.dps0340.attman

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Pair
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SelfdiagnosisActivity : AppCompatActivity() {
    private var symptomsList: ArrayList<Pair<String, String>>? = null
    private var flags: ArrayList<Int>? = null
    var call_number = 0
    private lateinit var tv_name: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selfdiagnosis_xml)
        tv_name = findViewById(R.id.tv_name5)!!
        val intent = intent
        val userName = intent.getStringExtra("userName")
        tv_name.text = userName
        constructSymptomsList()
        constructFlags()
        inflateSymptomsLayout()
    }

    private fun constructSymptomsList() {
        val englishSymptoms = arrayOf("Cough", "Fever", "Throat discomfort", "Headache", "Nasal congestion")
        val koreanSymptoms = arrayOf("기침", "37.5도 이상 열 또는 발열감", "인후통", "두통", "코막힘")
        symptomsList = ArrayList()
        for (i in englishSymptoms.indices) {
            val constructed = String.format("%s(%s)", koreanSymptoms[i], englishSymptoms[i])
            symptomsList!!.add(Pair(englishSymptoms[i], constructed))
        }
    }

    private fun inflateSymptomsLayout() {
        val size = symptomsList!!.size
        val inflater = layoutInflater
        val root = findViewById<ViewGroup>(R.id.SymptomsLayout)
        for (i in 0 until size) {
            inflater.inflate(R.layout.form_xml, root)
            val currentView = root.getChildAt(root.childCount - 1)
            val textView = currentView.findViewById<TextView>(R.id.symptom_textView)
            textView.text = symptomsList!![i].second
            val noButton = currentView.findViewById<Button>(R.id.no_button)
            val yesButton = currentView.findViewById<Button>(R.id.yes_button)
            noButton.setOnClickListener(ListenerFactory.clickListenerFactory(this, i, 2, Color.GREEN))
            yesButton.setOnClickListener(ListenerFactory.clickListenerFactory(this, i, 1, Color.RED))
        }
    }

    fun setSymptomsFlag(idx: Int, flag: Int) {
        if (flags!!.size <= idx) {
            return
        }
        flags!![idx] = flag
    }

    fun constructFlags() {
        flags = ArrayList()
        for (i in symptomsList!!.indices) {
            flags!!.add(0)
        }
    }

    private fun dispatchTakePictureIntent(callback: Intent) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            // asynchronous, non-blocking 상태
            // 수정 TODO
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            startActivity(callback)
        } catch (e: ActivityNotFoundException) {
            Log.e("ActivityNotFound", "ActivityNotFoundException!!")
            // display error state to the user
        }
    }

    fun call() {
        for (i in flags!!.indices) {
            if (flags!![i] != 2) {
                return
            }
        }
        var intent = intent
        val userName = intent.getStringExtra("userName")
        val userNumber = intent.getStringExtra("userNumber")
        val userEmail = intent.getStringExtra("userEmail")
        val userID = intent.getStringExtra("userID")
        intent = Intent(baseContext, ResultActivity::class.java)
        call_number++
        for (i in symptomsList!!.indices) {
            val p = symptomsList!![i]
            val name = p.first
            val flag = flags!![i]
            intent.putExtra(name, flag)
        }
        intent.putExtra("userName", userName)
        intent.putExtra("userNumber", userNumber)
        intent.putExtra("userID", userID)
        intent.putExtra("userEmail", userEmail)
        intent.putExtra("call_number", call_number)
        dispatchTakePictureIntent(intent)
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}