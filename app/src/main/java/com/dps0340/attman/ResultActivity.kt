package com.dps0340.attman

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ResultActivity : AppCompatActivity() {
    var result_number = 0
    lateinit var tv_name: TextView
    lateinit var button: Button
    lateinit var btn_emergencycall: Button
    val selections = listOf("아니오", "네")
    val symptoms = listOf("cough", "through", "head", "high", "nose")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        tv_name = findViewById(R.id.tv_name7)
        val userName = intent.getStringExtra("userName")
        tv_name.text = userName
        val answers = symptoms.map{ s -> intent.getIntExtra(s, 0)}
        for((a, s) in answers.zip(symptoms))
        {
            val id = resources.getIdentifier(s, "id", packageName)
            val view = findViewById<TextView>(id)
            view.text = selections[a]
        }
        btn_emergencycall = findViewById(R.id.btn_emergencycall3)
        val intent3 = getIntent()
//        val call_number = intent3.getIntExtra("call_number", 0)
//        if (call_number == 0) {
        // 조건 처리 TODO
            btn_emergencycall.setVisibility(View.GONE)
            button.setOnClickListener(View.OnClickListener { _ -> //TODO
                val intent2 = getIntent()
                val userName = intent2.getStringExtra("userName")
                val userNumber = intent2.getStringExtra("userNumber")
                val userEmail = intent2.getStringExtra("userEmail")
                val userID = intent2.getStringExtra("userID")
                val intent1 = Intent(baseContext, HomeActivity::class.java)
                result_number++
                intent1.putExtra("result_number", result_number)
                intent1.putExtra("userName", userName)
                intent1.putExtra("userNumber", userNumber)
                intent1.putExtra("userID", userID)
                intent1.putExtra("userEmail", userEmail)
                startActivity(intent1)
            })
//        } else if (call_number == 1) {
//            button.setVisibility(View.GONE)
//            btn_emergencycall.setOnClickListener(View.OnClickListener {
//                val intent2 = getIntent()
//                val userName = intent2.getStringExtra("userName")
//                val userNumber = intent2.getStringExtra("userNumber")
//                val userEmail = intent2.getStringExtra("userEmail")
//                val userID = intent2.getStringExtra("userID")
//                val intent1 = Intent(baseContext, Emergencycall::class.java)
//                intent1.putExtra("result_number", result_number)
//                intent1.putExtra("userName", userName)
//                intent1.putExtra("userNumber", userNumber)
//                intent1.putExtra("userID", userID)
//                intent1.putExtra("userEmail", userEmail)
//                startActivity(intent1)
//            })
//        }
    }
}