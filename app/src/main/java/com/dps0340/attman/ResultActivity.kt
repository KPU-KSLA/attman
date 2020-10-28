package com.dps0340.attman

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import org.jetbrains.anko.backgroundColor


class ResultActivity : AppCompatActivity() {
    private val selections = listOf("아니오", "네")
    private val symptoms = listOf("cough", "through", "head", "high", "nose")
    private val red = "#ffff0000"
    private val green = "#ff228b22"
    private val normalText = "홈화면으로 바로가기"
    private val emergencyText = "긴급연락 바로가기"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
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
        val userID = intent.getStringExtra("userID") ?: ""
        val isDangerous = intent.getBooleanExtra("dangerous?", false)
        val temp = intent.getDoubleExtra("temp", 0.0)
        val date = intent.getStringExtra("date") ?: ""
        val tempView = findViewById<TextView>(R.id.temp)
        tempView.text = temp.toString()
        val button = findViewById<Button>(R.id.btn)
        setButtonColor(button, isDangerous)
        setButtonText(button, isDangerous)
        val nextActivity = if(isDangerous) EmergencyCall::class.java else HomeActivity::class.java
        val destIntent = Intent(baseContext, nextActivity)
        val gson = Gson()
        val typeSignature = mutableMapOf<String, Boolean>()
        val result = gson.fromJson(intent.getStringExtra("result"), typeSignature.javaClass)
        val qr = intent.getStringExtra("qr") ?: ""
        markDiagnosed(userID)
        uploadDB(userID, temp, isDangerous, result, date, qr)
        destIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        destIntent.putExtra("dangerous?", isDangerous)
        destIntent.putExtra("userName", userName)
        destIntent.putExtra("userNumber", userNumber)
        destIntent.putExtra("userID", userID)
        destIntent.putExtra("userEmail", userEmail)
        button.setOnClickListener { _ ->
            startActivity(destIntent)
        }
    }
    private fun markDiagnosed(userID: String): Unit {
        val userRef = Firebase.database.getReference("userinfos/$userID")
        val isSelfDiagnosedListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userinfo: UserInfo? = dataSnapshot.getValue<UserInfo>()
                userinfo?.let {
                    userinfo.selfDiagnosed = true
                    userRef.setValue(userinfo)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("FIREBASE", "matchListener:onCancelled", databaseError.toException())
                // ...
            }
        }
        userRef.addListenerForSingleValueEvent(isSelfDiagnosedListener)
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
    private fun uploadDB(userID: String, temp: Double, isDangerous: Boolean, result: MutableMap<String, Boolean>, date: String, qr: String = "") {
        val ref = Firebase.database.reference.child("cases")
        val obj = ref.push()
        val key = obj.key
        key?.let {
            val case = Case(userID, temp, isDangerous, result, date, qr, false)
            ref.child(key).setValue(case)
        }
    }
}