package com.momtaz.tast128

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.momtaz.tast128.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    val REQUSET_CODE = 0
    val imageRE =Firebase.storage.reference
   lateinit var  currentFile :Uri
   var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding =ActivityMain2Binding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        callBack()

    }

    private fun callBack() {
        binding.imageView.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type ="image/*"
                startActivityForResult(it,REQUSET_CODE)
            }
        }
        binding.upload.setOnClickListener {

            upload("myImage${i}")
            i++
        }
        binding.download.setOnClickListener {
            downloadImage("myImage${i}")
            i++
        }
    }

    private fun downloadImage(fileName: String) {
        val maxDownloadSize = 5L * 1024 *1024
        val byte = imageRE.child("images/$fileName").getBytes(maxDownloadSize)
            .addOnCompleteListener { task->
                if (task.isSuccessful)
                {
                    val bmp =BitmapFactory.decodeByteArray(task.result,0,task.result!!.size)
                    binding.imageView.setImageBitmap(bmp)
                }

            }.addOnFailureListener{
                Toast.makeText(this@MainActivity2,it.message,Toast.LENGTH_LONG).show()
            }
    }

    private fun upload(fileName:String) {
        currentFile?.let {
            imageRE.child("images/$fileName").putFile(it)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Toast.makeText(this@MainActivity2,"upload is done !!",Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this@MainActivity2,it.message,Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK&&requestCode==REQUSET_CODE)
        {
            data?.data?.let {
                currentFile =it
                binding.imageView.setImageURI(currentFile)

            }
        }
    }
}