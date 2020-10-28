package com.dps0340.attman

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class AttendanceStatusActivity : AppCompatActivity() {
    private lateinit var tv_name: TextView
    private lateinit var tv_number: TextView
    private lateinit var tv_week: TextView
    private lateinit var tv_day: TextView
    private lateinit var tv_time: TextView
    private lateinit var tv_attendanceInformation: TextView

    // 파이어베이스 연동 TODO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.attendancestatus_xml)
        val database = Firebase.database
        val statusLayout = findViewById<LinearLayout>(R.id.StatusLayout)
        val inflater = layoutInflater
        val updateListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val case: Case? = dataSnapshot.getValue<Case>()
                case?.let {
                    inflater.inflate(R.layout.attendance_history_prefab, statusLayout)
                    val inflated = statusLayout.getChildAt(statusLayout.childCount - 1)
                    val userIDView = inflated.findViewById<TextView>(R.id.userID)
                    userIDView.text = userIDView.text.toString() + it.userID
                    val tempView = inflated.findViewById<TextView>(R.id.temp)
                    tempView.text = tempView.text.toString() + it.temp.toString()
                    val isDangerousView = inflated.findViewById<TextView>(R.id.isDangerous)
                    isDangerousView.text = listOf("위험하지 않음", "위험한 상태")[it.isDangerous.compareTo(false)]
                    val timeView = inflated.findViewById<TextView>(R.id.time)
                    val date = it.date
                    timeView.text = timeView.text.toString() + date
                    val qrView = inflated.findViewById<TextView>(R.id.qr)
                    qrView.text = it.qr
                    val checkByAdminView = inflated.findViewById<TextView>(R.id.checkByAdmin)
                    checkByAdminView.text = checkByAdminView.text.toString() + listOf("받지 않음", "받음")[it.isDangerous.compareTo(false)]
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("FIREBASE", "matchListener:onCancelled", databaseError.toException())
                // ...
            }
        }
        val casesRef = database.reference.child("cases")
        casesRef.addListenerForSingleValueEvent(updateListener)
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