package com.dps0340.attman

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_xml)
        val imageView = findViewById<ImageView>(R.id.kpu_logo) //한국산업기술대 로고 이미지뷰
        imageView.setImageResource(R.drawable.kpu_logo)
        val et_id = findViewById<EditText>(R.id.et_id)
        val et_pass = findViewById<EditText>(R.id.et_pass)
        val btn_register = findViewById<Button>(R.id.btn_register)
        btn_register.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        })
        val btn_login = findViewById<Button>(R.id.btn_login)
        val database = Firebase.database
        val userinfos = database.getReference("userinfos")
        btn_login.setOnClickListener(View.OnClickListener {
            val userID = et_id.text.toString()
            val userPassword = et_pass.text.toString()
            val responseListener: Response.Listener<String> = Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getBoolean("success")
                    if (success) { //로그인 성공시
                        val userID = jsonObject.getString("userID")
                        val userPassword = jsonObject.getString("userPassword")
                        val userName = jsonObject.getString("userName")
                        val userNumber = jsonObject.getString("userNumber")
                        val userEmail = jsonObject.getString("userEmail")
                        Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.putExtra("userID", userID)
                        intent.putExtra("userPassword", userPassword)
                        intent.putExtra("userName", userName)
                        intent.putExtra("userNumber", userNumber)
                        intent.putExtra("userEmail", userEmail)
                        startActivity(intent)
                    } else { //로그인 실패시
                        Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            val loginRequest = LoginRequest(userID, userPassword, responseListener)
            val queue = Volley.newRequestQueue(this@LoginActivity)
            queue.add(loginRequest)
        })
        requestPermissions()
    }

    private fun requestPermissions(): Unit {
        val permissions = arrayOf(Manifest.permission.INTERNET, Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        requestPermissions(permissions, 0)
    }
}