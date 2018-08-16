package com.example.potikorn.fuellibrary

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.potikorn.fuellibrary.coroutine.HttpCoroutine
import com.example.potikorn.fuellibrary.uploadimage.UploadImageActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNetworkWithForge.setOnClickListener {
            startActivity(Intent(this, HttpCoroutine::class.java))
        }

        btnUploadImage.setOnClickListener {
            startActivity(Intent(this, UploadImageActivity::class.java))
        }
    }
}