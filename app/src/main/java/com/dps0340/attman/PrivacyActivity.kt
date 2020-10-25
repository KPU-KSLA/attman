package com.dps0340.attman

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PrivacyActivity : AppCompatActivity() {
    private lateinit var tv_name: TextView
    private lateinit var tv_number: TextView
    private lateinit var tv_email: TextView
    private lateinit var tv_id: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.privacy_xml)
        tv_name = findViewById(R.id.tv_name3)!!
        tv_number = findViewById(R.id.tv_number3)!!
        tv_email = findViewById(R.id.tv_email3)!!
        tv_id = findViewById(R.id.tv_id3)!!
        val intent = intent
        val userName = intent.getStringExtra("userName")
        val userNumber = intent.getStringExtra("userNumber")
        val userEmail = intent.getStringExtra("userEmail")
        val userID = intent.getStringExtra("userID")
        tv_name.text = userName
        tv_number.text = userNumber
        tv_email.text = userEmail
        tv_id.text = userID
    }
}