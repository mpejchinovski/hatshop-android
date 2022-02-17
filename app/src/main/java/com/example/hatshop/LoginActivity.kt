package com.example.hatshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.hatshop.admin.AdminActivity
import com.example.hatshop.user.ShopActivity

class LoginActivity : AppCompatActivity() {
    private val firestore = FirebaseUtils().firestoreDatabase
    private val auth = FirebaseUtils().auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun handleLogin(v: View) {
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            run {
                if (task.isSuccessful) {
                    if (auth.currentUser != null) {
                        firestore.collection("users").document(auth.currentUser!!.uid).get().addOnSuccessListener {
                            run {
                                val intent = Intent()
                                var role = it.data?.get("role")
                                if (role == "admin") {
                                    intent.setClass(this, AdminActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    intent.setClass(this, ShopActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun handleRedirect(v: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}