package com.dps0340.attman

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    val diagnosedHolder = DianosedSingleton.obj;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_xml)

        //자기진단 버튼을 클릭하면 자기진단화면으로 전환
        val btn_selfdiagnosis = findViewById<Button>(R.id.btn_selfdaignosis2)
        btn_selfdiagnosis.setOnClickListener(View.OnClickListener {
            val currentIntent = intent
            val userName = currentIntent.getStringExtra("userName")
            val userNumber = currentIntent.getStringExtra("userNumber")
            val userEmail = currentIntent.getStringExtra("userEmail")
            val userID = currentIntent.getStringExtra("userID")
            val intent = Intent(this@HomeActivity, SelfDiagnosisActivity::class.java)
            intent.putExtra("userName", userName)
            intent.putExtra("userNumber", userNumber)
            intent.putExtra("userID", userID)
            intent.putExtra("userEmail", userEmail)
            startActivity(intent)
        })

        //출석관리 버튼을 클릭하면 출석체크화면으로 전환
        val btn_attendancecheck = findViewById<Button>(R.id.btn_attendancecheck2)
        btn_attendancecheck.setOnClickListener(View.OnClickListener {
            val currentIntent = intent
            val userName = currentIntent.getStringExtra("userName")
            val userNumber = currentIntent.getStringExtra("userNumber")
            val userEmail = currentIntent.getStringExtra("userEmail")
            val userID = currentIntent.getStringExtra("userID")
            val attendanceInformation = currentIntent.getStringExtra("attendanceInformation")
            val time = currentIntent.getStringExtra("time")
            val count = currentIntent.getIntExtra("count", 0)
            val diagnosed = diagnosedHolder.get()
            if (!diagnosed) {
                Toast.makeText(applicationContext, "자기진단을 먼저 해주시기 바랍니다.", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                val destIntent = Intent(this@HomeActivity, AttendancecheckActivity::class.java)
                destIntent.putExtra("userName", userName)
                destIntent.putExtra("userNumber", userNumber)
                destIntent.putExtra("userID", userID)
                destIntent.putExtra("userEmail", userEmail)
                destIntent.putExtra("attendanceInformation", attendanceInformation)
                destIntent.putExtra("time", time)
                destIntent.putExtra("count", count)
                startActivity(destIntent)
            }
        })

        //긴급연락 버튼을 클릭하면 긴급연락화면으로 전환
        val btn_emergencycall = findViewById<Button>(R.id.btn_emergenctycall2)
        btn_emergencycall.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@HomeActivity, EmergencyCall::class.java)
            startActivity(intent)
        })

        //실시간 확진자 정보를 알수 있는 사이트로 이동
        val btn_realtimeinformation = findViewById<Button>(R.id.btn_realtimeinformation)
        btn_realtimeinformation.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.confirm_cases_search_url)))
            startActivity(intent)
        })

        //로그아웃 기능
        val btn_logout = findViewById<Button>(R.id.btn_logout)
        btn_logout.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            startActivity(intent)
        })

        //개인정보관리 버튼을 클릭하면 개인정보 화면으로 전환
        val btn_privacy = findViewById<Button>(R.id.btn_privacy)
        btn_privacy.setOnClickListener(View.OnClickListener {
            val currentIntent = intent
            val userID = currentIntent.getStringExtra("userID")
            val userName = currentIntent.getStringExtra("userName")
            val userNumber = currentIntent.getStringExtra("userNumber")
            val userEmail = currentIntent.getStringExtra("userEmail")
            val destIntent = Intent(this@HomeActivity, PrivacyActivity::class.java)
            destIntent.putExtra("userID", userID)
            destIntent.putExtra("userName", userName)
            destIntent.putExtra("userNumber", userNumber)
            destIntent.putExtra("userEmail", userEmail)
            startActivity(destIntent)
        })
    }

}