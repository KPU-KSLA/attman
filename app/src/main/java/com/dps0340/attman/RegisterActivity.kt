package com.dps0340.attman

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_xml)
        val imageView = findViewById<ImageView>(R.id.kpu_logo) //한국산업기술대 로고 이미지 뷰
        imageView.setImageResource(R.drawable.kpu_logo)

        //아이디값 찾아주기
        val et_id = findViewById<EditText>(R.id.et_id2)
        val et_pass = findViewById<EditText>(R.id.et_pass2)
        val et_name = findViewById<EditText>(R.id.et_name2)
        val et_number = findViewById<EditText>(R.id.et_number2)
        val et_email = findViewById<EditText>(R.id.et_email2)

        //회원가입 버튼 클릭 시 수행
        val btm_register = findViewById<Button>(R.id.btn_register2)
        btm_register.setOnClickListener(View.OnClickListener {
            val userID = et_id.text.toString()
            val userPass = et_pass.text.toString()
            val userName = et_name.text.toString()
            val userNumber = et_number.text.toString().toInt()
            val userEmail = et_email.text.toString()
            val responseListener = Response.Listener<String?> { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val success = jsonObject.getBoolean("success")

                    //회원가입 성공시
                    if (success) {
                        Toast.makeText(applicationContext, "성공", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)

                        //회원가입 실패시
                    } else {
                        Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                        return@Listener
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            //서버로 Volley를 이용해서 요청
            val registerRequest = RegisterRequest(userID, userPass, userName, userNumber, userEmail, responseListener)
            val queue = Volley.newRequestQueue(this@RegisterActivity)
            queue.add(registerRequest)
        })
    }
}