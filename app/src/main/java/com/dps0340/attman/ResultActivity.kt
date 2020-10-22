package com.dps0340.attman

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.backgroundColor


class ResultActivity : AppCompatActivity() {
    lateinit var tv_name: TextView
    lateinit var button: Button
    val selections = listOf("아니오", "네")
    val symptoms = listOf("cough", "through", "head", "high", "nose")
    val red = "#ffff0000"
    val green = "#ff228b22"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        tv_name = findViewById(R.id.tv_name7)
        val userName = intent.getStringExtra("userName")
        tv_name.text = userName
        val answers = symptoms.map{ s -> intent.getIntExtra(s, 0)}
        for((a, s) in answers.zip(symptoms))
        {
            val id = resources.getIdentifier(s, "id", packageName)
            val view = findViewById<TextView>(id)
            view.text = selections[a]
        }
        val isDangerous = intent.getBooleanExtra("dangerous?", false)
        val button = findViewById<Button>(R.id.btn)
        setButtonColor(button, isDangerous)
        val nextActivity = if(isDangerous) EmergencyCall::class.java else HomeActivity::class.java
        button.setOnClickListener(View.OnClickListener { _ ->
            val currentIntent = intent
            val userName = currentIntent.getStringExtra("userName")
            val userNumber = currentIntent.getStringExtra("userNumber")
            val userEmail = currentIntent.getStringExtra("userEmail")
            val userID = currentIntent.getStringExtra("userID")
            val destIntent = Intent(baseContext, nextActivity)
            destIntent.putExtra("dangerous?", isDangerous)
            destIntent.putExtra("userName", userName)
            destIntent.putExtra("userNumber", userNumber)
            destIntent.putExtra("userID", userID)
            destIntent.putExtra("userEmail", userEmail)
            startActivity(destIntent)
        })
    }
    private fun setButtonColor(button: Button, isDangerous: Boolean) {
        val selectedColorString = if (isDangerous) red else green
        val selectedColor = Color.parseColor(selectedColorString)
        button.backgroundColor = selectedColor
    }
}