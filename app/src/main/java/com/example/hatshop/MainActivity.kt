package com.example.hatshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hatshop.admin.AdminActivity
import com.example.hatshop.user.ShopActivity

class MainActivity : AppCompatActivity() {
    val auth = FirebaseUtils().auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            if (auth.currentUser != null) {
                val intent = Intent(this, ShopActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        val intent = Intent(this, ShopActivity::class.java)
        startActivity(intent)
    }
}