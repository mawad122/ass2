package com.example.ass2

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fileName = "PDF"
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference


        fun showProgressDialog() {
            val view = layoutInflater.inflate(R.layout.progress, null)
            progressDialog = ProgressDialog(this)
            progressDialog.setCancelable(false)
            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.setContentView(view)
            progressDialog.show()
        }

        fun dismissProgressDialog() {
            if (::progressDialog.isInitialized && progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        }

        selectPdf.setOnClickListener{
            selectFile()

        }



        upload.setOnClickListener {
            showProgressDialog()
            val file: Uri = Uri.fromFile(File(fileName))
            val pdfRef = storageRef.child("pdfs/" + file.getLastPathSegment())
            val uploadTask = pdfRef.putFile(file)
            pdfRef.putFile(file).addOnSuccessListener {taskSnapshot ->
                dismissProgressDialog()
            }
        }


        download.setOnClickListener {
            val pdfRef = storageRef.child("pdfs/$fileName")
            val localFile = File(Environment.getExternalStorageDirectory(), fileName)
            pdfRef.getFile(localFile).addOnSuccessListener {taskSnapshot ->

            }

        }

    }

    private fun selectFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        ActivityCompat.startActivityForResult(intent, REQUEST_SELECT_PDF)
    }
}
