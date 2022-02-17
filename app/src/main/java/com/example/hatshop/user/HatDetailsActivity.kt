package com.example.hatshop.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.hatshop.FirebaseUtils
import com.example.hatshop.R
import com.example.hatshop.model.CartItem
import com.example.hatshop.model.Hat
import com.google.firebase.firestore.SetOptions
import com.squareup.picasso.Picasso

class HatDetailsActivity : AppCompatActivity() {
    private val auth = FirebaseUtils().auth

    private val firestore = FirebaseUtils().firestoreDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hat_details)
        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        val stock = intent.getIntExtra("stock", 0)
        val price = intent.getIntExtra("price", 0)
        val downloadURL = intent.getStringExtra("downloadURL")

        Picasso.get().load(downloadURL).into(findViewById<ImageView>(R.id.image))
        findViewById<TextView>(R.id.name).text = name
        findViewById<TextView>(R.id.stock).text = "Stock: ${stock}"
        findViewById<TextView>(R.id.price).text = "$${price}"

        findViewById<Button>(R.id.add_to_cart).setOnClickListener {
            var firestoreId = auth.currentUser!!.uid

            val cartItem = HashMap<String, CartItem>()
            if (id != null) {
                cartItem.put(id, CartItem(id, name, downloadURL, quantity = 1, price))
            }

            firestore.collection("carts").document(firestoreId).set(object { val items = cartItem }, SetOptions.merge()).addOnCompleteListener {
                Toast.makeText(this, "Hat added to cart.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ShopActivity::class.java)
                startActivity(intent)
            }
        }
    }
}