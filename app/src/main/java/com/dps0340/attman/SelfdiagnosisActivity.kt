package com.dps0340.attman

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Pair
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SelfdiagnosisActivity : AppCompatActivity() {
    val englishSymptoms = arrayOf("Cough", "Fever", "Throat discomfort", "Headache", "Nasal congestion")
    val koreanSymptoms = arrayOf("기침", "37.5도 이상 열 또는 발열감", "인후통", "두통", "코막힘")
    private val symptomsList = englishSymptoms.zip(koreanSymptoms.zip(englishSymptoms)
    {
        k, e -> "${k}(${e})"
    })
    private val flags = (symptomsList.indices).map {
        0
    }.toMutableList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selfdiagnosis_xml)
        val textView = findViewById<TextView>(R.id.tv_name5)!!
        val intent = intent
        val userName = intent.getStringExtra("userName")
        textView.text = userName
        inflateSymptomsLayout()
    }

    private fun inflateSymptomsLayout() {
        val size = symptomsList.size
        val inflater = layoutInflater
        val root = findViewById<ViewGroup>(R.id.SymptomsLayout)
        for (i in 0 until size) {
            inflater.inflate(R.layout.form_xml, root)
            val currentView = root.getChildAt(root.childCount - 1)
            val textView = currentView.findViewById<TextView>(R.id.symptom_textView)
            textView.text = symptomsList[i].second
            val noButton = currentView.findViewById<Button>(R.id.no_button)
            val yesButton = currentView.findViewById<Button>(R.id.yes_button)
            noButton.setOnClickListener(ListenerFactory.makeClickListener(this, i, 2, Color.GREEN))
            yesButton.setOnClickListener(ListenerFactory.makeClickListener(this, i, 1, Color.RED))
        }
    }

    fun setSymptomsFlag(idx: Int, flag: Int) {
        if (flags.size <= idx) {
            return
        }
        flags[idx] = flag
    }

    lateinit var currentPhotoPath: String

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    val REQUEST_TAKE_PHOTO = 1

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.android.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    fun call() {
        if(flags.any{ e -> e == 0 }) {
            return
        }
        var intent = intent
        val userName = intent.getStringExtra("userName")
        val userNumber = intent.getStringExtra("userNumber")
        val userEmail = intent.getStringExtra("userEmail")
        val userID = intent.getStringExtra("userID")
        intent = Intent(baseContext, ResultActivity::class.java)
        for (i in symptomsList.indices) {
            val p = symptomsList[i]
            val name = p.first
            val flag = flags[i]
            intent.putExtra(name, flag)
        }
        intent.putExtra("userName", userName)
        intent.putExtra("userNumber", userNumber)
        intent.putExtra("userID", userID)
        intent.putExtra("userEmail", userEmail)
        val isDangerous = flags.any {
            e -> e == 1
        }
        intent.putExtra("dangerous?", isDangerous)
        dispatchTakePictureIntent()
        startActivity(intent)
    }
}