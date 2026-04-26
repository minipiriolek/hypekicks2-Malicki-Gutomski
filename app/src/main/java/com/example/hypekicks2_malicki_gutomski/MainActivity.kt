package com.example.hypekicks2_malicki_gutomski

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, StorefrontActivity::class.java)
        startActivity(intent)
        finish()
    }
}