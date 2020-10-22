package com.dps0340.attman

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_xml)
        requestPermissions()
        FirebaseApp.initializeApp(baseContext)
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
        btn_login.setOnClickListener(View.OnClickListener {
            val userID = et_id.text.toString()
            val rawUserPassword = et_pass.text.toString()
            val encryptedPassword = Sha512.encrypt(rawUserPassword)
            val matchListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user: UserInfo? = dataSnapshot.getValue<UserInfo>()
                    user?.let {
                        if (it.id == userID && it.password == encryptedPassword) {
                            loginSuccess(it)
                        } else {
                            toast("로그인 실패")
                            return
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("FIREBASE", "matchListener:onCancelled", databaseError.toException())
                    // ...
                }
            }
            val userRef = database.getReference("userinfos/$userID")
            userRef.addListenerForSingleValueEvent(matchListener)
        })
    }

    private fun loginSuccess(user: UserInfo) {
        Toast.makeText(applicationContext, "로그인 성공", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.putExtra("userID", user.id)
        intent.putExtra("userPassword", user.password)
        intent.putExtra("userName", user.name)
        intent.putExtra("userNumber", user.stdNum)
        intent.putExtra("userEmail", user.email)
        startActivity(intent)
    }

    private fun requestPermissions(): Unit {
        val permissions = arrayOf(Manifest.permission.INTERNET, Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        requestPermissions(permissions, 0)
    }
}