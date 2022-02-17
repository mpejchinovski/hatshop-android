package com.example.hatshop.admin

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.hatshop.FirebaseUtils
import com.example.hatshop.R
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class AddHatActivity : AppCompatActivity() {
    private val firestore = FirebaseUtils().firestoreDatabase
    private lateinit var chooseImageBtn : Button
    private lateinit var uploadImageBtn : Button
    private lateinit var submitBtn : Button
    private lateinit var image : ImageView
    private lateinit var ImageURI : Uri
    private val storage = FirebaseUtils().storage
    private lateinit var downloadURL : String

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_hat)

        chooseImageBtn = findViewById(R.id.choose_image)
        uploadImageBtn = findViewById(R.id.upload_image)
        submitBtn = findViewById(R.id.submit)
        image = findViewById(R.id.image)

        chooseImageBtn.setOnClickListener {
            chooseImage()
        }

        uploadImageBtn.setOnClickListener {
            uploadImage()
        }

        submitBtn.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        val name = findViewById<EditText>(R.id.hat_name).text.toString()
        val price = findViewById<EditText>(R.id.hat_price).text.toString().toInt()
        val stock = findViewById<EditText>(R.id.hat_stock).text.toString().toInt()
        val url = downloadURL

       if (downloadURL != null) {
           firestore.collection("hats").add(object {
               val name = name
               val price = price
               val stock = stock
               val downloadURL = url
           }).addOnSuccessListener {
               run {
                   Toast.makeText(this@AddHatActivity, "Hat added", Toast.LENGTH_SHORT).show()
                   val intent = Intent(this, AdminActivity::class.java)
                   startActivity(intent)
               }
           }
       }
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun uploadImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val name = findViewById<EditText>(R.id.hat_name).text.toString()

        val formatter = SimpleDateFormat("dd_mm_YYYY", Locale.getDefault())
        val now = Date()
        val fileName : String

        if (name !== null) {
            fileName = name
        } else {
            fileName = formatter.format(now)
        }

        val storageReference = storage.getReference("images/${fileName}")

        storageReference.putFile(ImageURI).addOnSuccessListener {
            run {
                Toast.makeText(this@AddHatActivity, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
                storageReference.downloadUrl.addOnSuccessListener {
                    run {
                        downloadURL = it.toString()
                    }
                }
            }
        }.addOnFailureListener{
            run {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }

                Toast.makeText(this@AddHatActivity, "Upload failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            ImageURI = data?.data!!
            image.setImageURI(ImageURI)
        }
    }
}