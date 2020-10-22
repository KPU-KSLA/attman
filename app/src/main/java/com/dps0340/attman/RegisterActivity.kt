package com.dps0340.attman

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import org.jetbrains.anko.toast
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
        val database = Firebase.database
        btm_register.setOnClickListener(View.OnClickListener {
            val userID = et_id.text.toString()
            val rawPassword = et_pass.text.toString()
            val userName = et_name.text.toString()
            val userNumber = et_number.text.toString()
            val email = et_email.text.toString()
            val user = UserInfoBuilder.build(userID, rawPassword, userName, userNumber, email)
            val ref = database.getReference("userinfos/$userID")
            val existsListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(!dataSnapshot.exists()) {
                        ref.setValue(user)
                        finish()
                    } else {
                        toast("중복된 id입니다.")
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("FIREBASE", "existsListener:onCancelled", databaseError.toException())
                    // ...
                }
            }
            ref.addListenerForSingleValueEvent(existsListener)
        })
    }
}