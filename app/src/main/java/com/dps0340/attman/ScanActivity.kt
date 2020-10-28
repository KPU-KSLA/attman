package com.dps0340.attman

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.zxing.integration.android.IntentIntegrator
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import java.io.File
import java.io.IOException
import java.sql.Timestamp
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
    private var QRCompleted = false
    private var OCRCompleted = false

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

        val destIntent = Intent(baseContext, ResultActivity::class.java)
        destIntent.putExtra("userName", userName)
        destIntent.putExtra("userNumber", userNumber)
        destIntent.putExtra("userID", userID)
        destIntent.putExtra("userEmail", userEmail)
        destIntent.putExtra("dangerous?", isDangerous)
        destIntent.putExtra("result", resultJson)
        preparedIntent = destIntent
        OCRButton.setOnClickListener {
            dispatchTakePictureIntent()
        }
        QRButton.setOnClickListener {
            dispatchQRIntent()
        }
        FinishButton.setOnClickListener {
            if(OCRCompleted && QRCompleted) {
                val timeStamp = Timestamp(System.currentTimeMillis()).toString()
                preparedIntent.putExtra("date", timeStamp)
                startActivity(preparedIntent)
            } else {
                toast("먼저 인식을 수행해 주세요.")
            }
        }
    }

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_QR_CAPTURE = 2

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
                                OCRCompleted = true
                                makeCompleteText(OCRButton)
                                checkCompletableAndIfEditText()
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
        if (requestCode == REQUEST_QR_CAPTURE && resultCode == RESULT_OK) {
            processQRResult(requestCode, resultCode, data)
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
            File(path).delete()
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

    private fun processQRResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(resultCode, data)
        if (result == null || result.contents == null) {
            toast("QR코드를 다시 스캔하여 주세요")
            super.onActivityResult(requestCode, resultCode, data)
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
//        longToast("Scanned Text: $result.contents")
        val content = result.contents
        preparedIntent.putExtra("qr", content)
        QRCompleted = true
        makeCompleteText(QRButton)
        checkCompletableAndIfEditText()
        QRButton.setOnClickListener {
            toast("이미 완료되었습니다.")
        }
    }

    private fun dispatchQRIntent() {
        IntentIntegrator(this).setRequestCode(REQUEST_QR_CAPTURE).initiateScan()
    }

    private fun checkCompletableAndIfEditText() {
        if(OCRCompleted && QRCompleted) {
            FinishButton.text = complete
        }
    }

    private fun makeCompleteText(button: Button) {
        button.text = button.text.toString() + completePostFix
    }
}