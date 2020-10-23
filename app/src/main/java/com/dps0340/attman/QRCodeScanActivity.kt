package com.dps0340.attman

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class QRCodeScanActivity : AppCompatActivity() {
    private var edt: EditText? = null
    private var edt2: TextView? = null
    private var image_check: ImageView? = null
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qrscan_xml)
        edt = findViewById(R.id.edt_attendancecheck)
        edt2 = findViewById(R.id.edt2)
        image_check = findViewById(R.id.image_check)
        IntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                edt!!.setText("QR코드를 다시 스캔하여 주세요!")
                edt!!.setTextColor(Color.RED)
                image_check!!.visibility = View.INVISIBLE
                edt2!!.visibility = View.GONE

                // todo
            } else {
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                // todo
                edt!!.setText(result.contents + " 출석이 완료되었습니다!")
                edt2!!.visibility = View.INVISIBLE
                edt2!!.text = result.contents
                val attendanceInformation = result.contents
                val now = System.currentTimeMillis()
                val mDate = Date(now)
                val simpleData = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                val time = simpleData.format(mDate)
//                val responseListener: Response.Listener<String?>? = Response.Listener { response ->
//                    try {
//                        val jsonObject = JSONObject(response)
//                        val success = jsonObject.getBoolean("success")
//                        //출석 성공시
//                        if (success) {
//                            //String time = jsonObject.getString("time");
//                            //String attendanceInformation2 = jsonObject.getString("attendanceInformation");
//                            val attendanceInformation = edt2!!.text.toString()
//                            val now = System.currentTimeMillis()
//                            val mDate = Date(now)
//                            val simpleData = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
//                            val time = simpleData.format(mDate)
//                            val intent2 = intent
//                            val userName = intent2.getStringExtra("userName")
//                            val userNumber = intent2.getStringExtra("userNumber")
//                            val userEmail = intent2.getStringExtra("userEmail")
//                            val userID = intent2.getStringExtra("userID")
//                            Toast.makeText(applicationContext, " 출석정보 저장성공", Toast.LENGTH_SHORT).show()
//                            val intent = Intent(this@QrcodescanActivity, AttendancestatusActivity::class.java)
//                            count++
//                            intent.putExtra("userName", userName)
//                            intent.putExtra("userNumber", userNumber)
//                            intent.putExtra("userID", userID)
//                            intent.putExtra("userEmail", userEmail)
//                            intent.putExtra("attendanceInformation", attendanceInformation)
//                            intent.putExtra("time", time)
//                            intent.putExtra("count", count)
//                            startActivity(intent)
//
//
//                            //출석 실패시
//                        } else {
//                            Toast.makeText(applicationContext, "출석정보 저장실패", Toast.LENGTH_SHORT).show()
//                            return@Listener
//                        }
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                }

//                //서버로 Volley를 이용해서 요청
//                val attendanceRequest = AttendanceRequest(attendanceInformation, time, responseListener)
//                val queue = Volley.newRequestQueue(this@QrcodescanActivity)
//                queue.add(attendanceRequest)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}