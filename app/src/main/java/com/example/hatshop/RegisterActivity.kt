package com.example.hatshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.hatshop.user.ShopActivity

class RegisterActivity : AppCompatActivity() {
    private val firebaseUtils = FirebaseUtils()
    private val auth = firebaseUtils.auth
    private val firestore = firebaseUtils.firestoreDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
    
    fun handleRedirect(v: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun handleRegister(v: View) {
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()
        val firstName = findViewById<EditText>(R.id.firstName).text.toString()
        val lastName = findViewById<EditText>(R.id.lastName).text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            task -> run {
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        firestore.collection("users").document(user.uid).set(object {
                            val firstName = firstName
                            val lastName = lastName
                            val role = "user"
                        })

                        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                            run {
                                val intent = Intent()
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