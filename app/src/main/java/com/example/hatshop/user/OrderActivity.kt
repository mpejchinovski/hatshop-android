package com.example.hatshop.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.hatshop.FirebaseUtils
import com.example.hatshop.R
import com.example.hatshop.adapter.CartItemAdapter
import com.example.hatshop.model.Cart
import com.example.hatshop.model.CartItem
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions

class OrderActivity : AppCompatActivity() {
    private val firestore = FirebaseUtils().firestoreDatabase
    private val auth = FirebaseUtils().auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        auth.currentUser?.let {
            firestore.collection("carts").document(it.uid).addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    val items = snapshot.toObject(Cart::class.java)?.items
                    val totalHats = ArrayList(items?.values).size
                    val totalPrice = ArrayList(items?.values).fold(0) { sum, element -> sum + element.price!! * element.quantity!! }
                    Log.d("total", totalPrice.toString())
                    findViewById<TextView>(R.id.hats_total).text = "Items: ${totalHats}"
                    findViewById<TextView>(R.id.order_total).text = "Order total: $${totalPrice}"

                    findViewById<Button>(R.id.process_order).setOnClickListener {
                        auth.currentUser?.let { it1 ->
                            firestore.collection("orders").document(it1.uid).set(object {
                                val items = items
                                val totalPrice = totalPrice
                                val totalHats = totalHats
                            }).addOnCompleteListener {
                                if (items != null) {
                                    for (id in items.keys) {
                                        firestore.collection("hats").document(id).set(object {
                                            val stock = FieldValue.increment(-1)
                                        }, SetOptions.merge()).addOnCompleteListener {
                                            firestore.collection("carts").document(auth.currentUser!!.uid).set(object {
                                                val items = arrayListOf<CartItem>()
                                            }).addOnCompleteListener {
                                                val intent = Intent(this, ShopActivity::class.java)
                                                startActivity(intent)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}