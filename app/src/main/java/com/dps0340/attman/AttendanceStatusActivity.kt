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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.attendancestatus_xml)
        val database = Firebase.database
        val statusLayout = findViewById<LinearLayout>(R.id.StatusLayout)
        val inflater = layoutInflater
        val userID = intent.getStringExtra("userID") ?: ""
        val updateListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val case: Case? = userSnapshot.getValue<Case>()
                    case?.let {
                        if(it.userID != userID) {
                            return@let
                        }
                        inflater.inflate(R.layout.attendance_history_prefab, statusLayout)
                        val inflated = statusLayout.getChildAt(statusLayout.childCount - 1)
                        val userIDView = inflated.findViewById<TextView>(R.id.userID)
                        userIDView.text = "${userIDView.text}${it.userID}"
                        val tempView = inflated.findViewById<TextView>(R.id.temp)
                        tempView.text = "${tempView.text}${it.temp}"
                        val isDangerousView = inflated.findViewById<TextView>(R.id.isDangerous)
                        isDangerousView.text = listOf("위험하지 않음", "위험한 상태")[it.isDangerous.compareTo(false)]
                        val timeView = inflated.findViewById<TextView>(R.id.time)
                        val date = it.date
                        timeView.text = "${timeView.text}$date"
                        val qrView = inflated.findViewById<TextView>(R.id.qr)
                        qrView.text = it.qr
                        val checkByAdminView = inflated.findViewById<TextView>(R.id.checkByAdmin)
                        checkByAdminView.text = "${checkByAdminView.text}${listOf("받지 않음", "받음")[it.checkedByAdmin.compareTo(false)]}"
                    }
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
        statusLayout.invalidate()
    }

    //백버튼을 눌렀을때 기능
    override fun onBackPressed() {
        val currentIntent = intent
        val strings = IntentArgumentHandler.getStrings(currentIntent, listOf("userName", "userNumber", "attendanceInformation", "time"))
        val count = currentIntent.getIntExtra("count", 0)
        val prevIntent = Intent()
        IntentArgumentHandler.putStrings(prevIntent, strings)
        prevIntent.putExtra("count", count)
        setResult(RESULT_OK, prevIntent)
        finish()
    }
}