package com.dps0340.attman

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class EmergencyCall : AppCompatActivity() {
    private val telNumbers = listOf<String>("1339", "031-310-5830", "031-310-5901", "031-8041-0086", "031-8041-1030")
        .map {
            s -> "tel${s}"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emergencycall_xml)

        for((number, idx) in telNumbers.zip(0..telNumbers.size)) {
            val call = findViewById<Button>(resources.getIdentifier("btn_call${idx}", "id", packageName))
            val dialogCall = findViewById<Button>(resources.getIdentifier("btn_dialogcall${idx}", "id", packageName))
            call.setOnClickListener { _ ->
                startActivity(Intent("android.intent.action.CALL", Uri.parse(number)))
            }
            dialogCall.setOnClickListener { _ ->
                startActivity(Intent("android.intent.action.DIAL", Uri.parse(number)))
            }
        }
    }

    override fun onBackPressed() {
        val currentIntent = intent
        val userName = currentIntent.getStringExtra("userName")
        val userNumber = currentIntent.getStringExtra("userNumber")
        val userEmail = currentIntent.getStringExtra("userEmail")
        val userID = currentIntent.getStringExtra("userID")
        val destIntent = Intent(this@EmergencyCall, HomeActivity::class.java)
        destIntent.putExtra("userName", userName)
        destIntent.putExtra("userNumber", userNumber)
        destIntent.putExtra("userID", userID)
        destIntent.putExtra("userEmail", userEmail)
        startActivity(destIntent)
        super.onBackPressed()
    }
}