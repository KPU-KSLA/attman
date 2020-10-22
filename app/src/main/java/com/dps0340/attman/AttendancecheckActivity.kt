package com.dps0340.attman

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AttendancecheckActivity : AppCompatActivity() {
    private lateinit var btn_qrcodescan: Button
    private lateinit var btn_attendancestatus: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.attendancecheck_xml)

        //버튼클릭시 qr코드 스캔화면으로 넘어가는 기능
        btn_qrcodescan = findViewById(R.id.btn_qrcodescan)
        btn_qrcodescan.setOnClickListener(View.OnClickListener {
            val intent2 = intent
            val userName = intent2.getStringExtra("userName")
            val userNumber = intent2.getStringExtra("userNumber")
            val userEmail = intent2.getStringExtra("userEmail")
            val userID = intent2.getStringExtra("userID")
            val intent = Intent(this@AttendancecheckActivity, QrcodescanActivity::class.java)
            intent.putExtra("userName", userName)
            intent.putExtra("userNumber", userNumber)
            intent.putExtra("userID", userID)
            intent.putExtra("userEmail", userEmail)
            startActivity(intent)
        })

        //버튼클릭시 출석현황 화면으로 넘어가는 기능
        btn_attendancestatus = findViewById(R.id.btn_attendancestatus)
        btn_attendancestatus.setOnClickListener(View.OnClickListener {
            val intent2 = intent
            val userName = intent2.getStringExtra("userName")
            val userNumber = intent2.getStringExtra("userNumber")
            val userEmail = intent2.getStringExtra("userEmail")
            val userID = intent2.getStringExtra("userID")
            val attendanceInformation = intent2.getStringExtra("attendanceInformation")
            val time = intent2.getStringExtra("time")
            val count = intent2.getIntExtra("count", 0)
            val intent = Intent(this@AttendancecheckActivity, AttendancestatusActivity::class.java)
            intent.putExtra("userName", userName)
            intent.putExtra("userNumber", userNumber)
            intent.putExtra("userID", userID)
            intent.putExtra("userEmail", userEmail)
            intent.putExtra("attendanceInformation", attendanceInformation)
            intent.putExtra("time", time)
            intent.putExtra("count", count)
            startActivity(intent)
        })
    }

    //백버튼을 눌렀을때 기능
    override fun onBackPressed() {
        val intent2 = intent
        val userName = intent2.getStringExtra("userName")
        val userNumber = intent2.getStringExtra("userNumber")
        val userEmail = intent2.getStringExtra("userEmail")
        val userID = intent2.getStringExtra("userID")
        val attendanceInformation = intent2.getStringExtra("attendanceInformation")
        val time = intent2.getStringExtra("time")
        val count = intent2.getIntExtra("count", 0)
        val intent = Intent(this@AttendancecheckActivity, HomeActivity::class.java)
        intent.putExtra("userName", userName)
        intent.putExtra("userNumber", userNumber)
        intent.putExtra("userID", userID)
        intent.putExtra("userEmail", userEmail)
        intent.putExtra("attendanceInformation", attendanceInformation)
        intent.putExtra("time", time)
        intent.putExtra("count", count)
        startActivity(intent)
        super.onBackPressed()
    }
}