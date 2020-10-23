package com.dps0340.attman

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ScanActivity : AppCompatActivity() {

    private val notFinished = "필수 항목을 수행해주세요."
    private val completePostFix = " - 완료"
    private val complete = "완료!"
    private lateinit var preparedIntent: Intent
    private val regexPattern = "\\d+[.]\\d+".toRegex()
    private lateinit var OCRButton: Button
    private lateinit var QRButton: Button
    private lateinit var FinishButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        OCRButton = findViewById<Button>(R.id.OCRButton)
        QRButton = findViewById<Button>(R.id.QRButton)
        FinishButton = findViewById<Button>(R.id.FinishButton)

        val currentIntent = intent
        val userName = currentIntent.getStringExtra("userName")
        val userNumber = currentIntent.getStringExtra("userNumber")
        val userID = currentIntent.getStringExtra("userID")
        val userEmail = currentIntent.getStringExtra("userEmail")
        val isDangerous = currentIntent.getBooleanExtra("dangerous?", false)
        val resultJson = currentIntent.getStringExtra("result")

        FinishButton.text = notFinished

        val destIntent = Intent(baseContext, HomeActivity::class.java)
        destIntent.putExtra("userName", userName)
        destIntent.putExtra("userNumber", userNumber)
        destIntent.putExtra("userID", userID)
        destIntent.putExtra("userEmail", userEmail)
        destIntent.putExtra("dangerous?", isDangerous)
        preparedIntent = destIntent
        intent.putExtra("result", resultJson)
        OCRButton.setOnClickListener {
            dispatchTakePictureIntent()
        }
        QRButton.setOnClickListener {
            toast("TODO")
        }
        FinishButton.setOnClickListener {
            toast("먼저 체온계 인식을 수행해 주세요.")
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            MediaScannerConnection.scanFile(this, arrayOf(f.toString()),
                    arrayOf(f.name)) { path, uri ->
                run {
                    val failureCallback = { ->
                        run {
                            toast("체온이 식별되지 않았습니다.")
                            return@run
                        }
                    }
                    val successCallback = { s: String ->
                        run {
                            try {
                                val num = s.toDouble()
                                if (25.0 > num || 45.0 < num) {
                                    throw InputMismatchException()
                                }
                                if(32.0 > num || 38.0 < num) {
                                    intent.putExtra("dangerous?", true)
                                }
                                preparedIntent.putExtra("temp", num)
                                preparedIntent.putExtra("imgUri", uri)
                                makeCompletable()
                                makeCompleteText(OCRButton)
                                OCRButton.setOnClickListener {
                                    toast("이미 완료되었습니다.")
                                }
                                return@run
                            }  catch (e: Exception) {
                                failureCallback()
                            }
                        }
                    }
                    parseFloatWithCallback(path, successCallback, failureCallback)
                }

            }
        }
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
            galleryAddPic()
        }
    }

    private fun parseFloatWithCallback(path: String, successCallback: (String)->Unit, failureCallback: ()->Unit): Unit {
        val image = InputImage.fromFilePath(this, Uri.fromFile(File(path)))
        val recognizer = TextRecognition.getClient()
        recognizer.process(image).addOnSuccessListener { visionText -> run {
            Log.i("RECOGNIZER", "Original Text: ${visionText.text}")
            val find = regexPattern.find(visionText.text)
            if(find == null) {
                Log.i("RECOGNIZER", "Couldn't find Parsed Decimal")
                failureCallback()
                return@run
            }
            val value = find.value
            Log.i("RECOGNIZER", "Parsed Decimal: $value")
            successCallback(value)
        }
        }.addOnFailureListener { _ -> run {
            failureCallback()
        }
        }
    }
    val REQUEST_TAKE_PHOTO = 1

    private fun dispatchTakePictureIntent() {
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

    private fun makeCompletable() {
        FinishButton.text = complete
    }

    private fun makeCompleteText(button: Button) {
        button.text = button.text.toString() + completePostFix
    }
}