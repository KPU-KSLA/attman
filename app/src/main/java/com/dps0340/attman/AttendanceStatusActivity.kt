package com.dps0340.attman

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AttendanceStatusActivity : AppCompatActivity() {
    //public static final String PREFS_NAME = "MyPrefs";
    private lateinit var tv_name: TextView
    private lateinit var tv_number: TextView
    private lateinit var tv_week: TextView
    private lateinit var tv_day: TextView
    private lateinit var tv_time: TextView
    private lateinit var tv_attendanceInformation: TextView

    //String attendanceInformation;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.attendancestatus_xml)
        tv_name = findViewById(R.id.tv_name6)
        tv_number = findViewById(R.id.tv_number6)
        tv_week = findViewById(R.id.tv_week)
        tv_day = findViewById(R.id.tv_day)
        tv_time = findViewById(R.id.tv_time)
        tv_attendanceInformation = findViewById(R.id.tv_attendanceInformation)
        val intent = intent
        val userName = intent.getStringExtra("userName")
        val userNumber = intent.getStringExtra("userNumber")
        val attendanceInformation = intent.getStringExtra("attendanceInformation")
        val time = intent.getStringExtra("time")
        val count = intent.getIntExtra("count", 0)
        val currenTime = Calendar.getInstance().time
        val date_text = SimpleDateFormat("EE요일", Locale.getDefault()).format(currenTime)
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdfNow = SimpleDateFormat("yyyy-MM-dd")
        val formatData = sdfNow.format(date)
        val thread: Thread = object : Thread() {
            override fun run() {
                while (!isInterrupted) {
                    runOnUiThread {
                        val calendar = Calendar.getInstance() // 칼렌다 변수
                        val hour = calendar[Calendar.HOUR_OF_DAY] // 시
                        val minute = calendar[Calendar.MINUTE] // 분
                        val second = calendar[Calendar.SECOND] // 초
                        tv_time.text = "($hour:$minute:$second)"
                    }
                    try {
                        sleep(1000) // 1000 ms = 1초
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                } // while
            } // run()
        } // new Thread() { };
        thread.start()
        tv_name.text = userName
        tv_number.text = "[$userNumber]"
        tv_week.text = date_text
        tv_day.text = formatData
        tv_attendanceInformation.text = "$attendanceInformation   $time 출석완료$count"

        //SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
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
        val intent = Intent(this@AttendanceStatusActivity, HomeActivity::class.java)
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