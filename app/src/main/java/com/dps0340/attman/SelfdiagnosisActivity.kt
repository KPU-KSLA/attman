package com.dps0340.attman

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.google.gson.Gson
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import org.jetbrains.anko.toast
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class SelfdiagnosisActivity : AppCompatActivity() {
    private val englishSymptoms = arrayOf("Cough", "Fever", "Throat discomfort", "Headache", "Nasal congestion")
    private val koreanSymptoms = arrayOf("기침", "37.5도 이상 열 또는 발열감", "인후통", "두통", "코막힘")
    private var preparedIntent: Intent? = null
    private val symptomsList = englishSymptoms.zip(koreanSymptoms.zip(englishSymptoms)
    { k, e ->
        "${k}(${e})"
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

    private lateinit var currentPhotoPath: String

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            preparedIntent?.let {
                galleryAddPic(it)
            }
        }
    }

    private fun parseFloatWithCallback(path: String, callBack: (String)->Unit): Unit {
        val image = InputImage.fromFilePath(this, Uri.fromFile(File(path)))
        val recognizer = TextRecognition.getClient()
        recognizer.process(image).addOnSuccessListener { visionText -> run {
                try {
                    callBack(visionText.text)
                } catch (e: Exception) {
                }
            }
        }.addOnFailureListener {

        }
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun galleryAddPic(callBackIntent: Intent) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            MediaScannerConnection.scanFile(this, arrayOf(f.toString()),
            arrayOf(f.name)) { path, uri ->
                run {
                    val callBack = { s: String ->
                        run {
                            try {
                                val num = s.toDouble()
                                if (25.0 > num || 45.0 < num) {
                                    toast("체온이 식별되지 않았습니다.")
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        dispatchTakePictureIntent(intent)
                                    }, 2000)
                                    return@run
                                }
                                callBackIntent.putExtra("temp", num)
                                callBackIntent.putExtra("imgUri", uri)
                                startActivity(callBackIntent)
                            }  catch (e: Exception) { }
                        }
                    }
                        parseFloatWithCallback(path, callBack)
                }

            }
        }
    }

    val REQUEST_TAKE_PHOTO = 1

    private fun dispatchTakePictureIntent(callbackIntent: Intent) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE).also { takePictureIntent ->
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
        val isDangerous = flags.any { e -> e == 1 }
        intent.putExtra("dangerous?", isDangerous)
        val gson = Gson()
        intent.putExtra("result", gson.toJson(flags))
        preparedIntent = intent
        dispatchTakePictureIntent(intent)
    }
}