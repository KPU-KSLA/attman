package com.dps0340.attman

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.KeyCharacterMap
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.backgroundColor
import org.w3c.dom.Text


class ResultActivity : AppCompatActivity() {
    val selections = listOf("아니오", "네")
    val symptoms = listOf("cough", "through", "head", "high", "nose")
    val red = "#ffff0000"
    val green = "#ff228b22"
    val normalText = "홈화면으로 바로가기"
    val emergencyText = "긴급연락 바로가기"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        markDiagnosed()
        val tv_name = findViewById<TextView>(R.id.tv_name7)
        val userName = intent.getStringExtra("userName")
        tv_name.text = userName
        val answers = symptoms.map{ s -> intent.getIntExtra(s, 0)}
        for((a, s) in answers.zip(symptoms))
        {
            val id = resources.getIdentifier(s, "id", packageName)
            val view = findViewById<TextView>(id)
            view.text = selections[a]
        }
        val userNumber = intent.getStringExtra("userNumber")
        val userEmail = intent.getStringExtra("userEmail")
        val userID = intent.getStringExtra("userID")
        val imgUri = intent.getStringExtra("imgUri")
        val isDangerous = intent.getBooleanExtra("dangerous?", false)
        val button = findViewById<Button>(R.id.btn)
        setButtonColor(button, isDangerous)
        setButtonText(button, isDangerous)
        val nextActivity = if(isDangerous) EmergencyCall::class.java else HomeActivity::class.java
        val destIntent = Intent(baseContext, nextActivity)
        val result = Json.
        destIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        destIntent.putExtra("dangerous?", isDangerous)
        destIntent.putExtra("userName", userName)
        destIntent.putExtra("userNumber", userNumber)
        destIntent.putExtra("userID", userID)
        destIntent.putExtra("userEmail", userEmail)
        button.setOnClickListener(View.OnClickListener { _ ->
            startActivity(destIntent)
        })
    }
    private fun markDiagnosed(): Unit {
        DianosedSingleton.obj.set(true);
    }
    private fun setButtonColor(button: Button, isDangerous: Boolean): Unit {
        val selectedColorString = if (isDangerous) red else green
        val selectedColor = Color.parseColor(selectedColorString)
        button.backgroundColor = selectedColor
    }
    private fun setButtonText(button: Button, isDangerous: Boolean): Unit {
        val selectedText = if (isDangerous) emergencyText else normalText
        button.text = selectedText
    }
    private fun uploadDB(userID: String, imgUri: Uri, Qr: String, isDangerous: Boolean, result: List<Boolean>) {

    }
}