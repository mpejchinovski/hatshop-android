package com.example.hatshop.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hatshop.FirebaseUtils
import com.example.hatshop.R
import com.example.hatshop.adapter.CartItemAdapter
import com.example.hatshop.model.Cart
import com.example.hatshop.model.CartItem

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var cartItemArrayList : ArrayList<CartItem>
    private lateinit var itemAdapter: CartItemAdapter
    private val firestore = FirebaseUtils().firestoreDatabase
    private val auth = FirebaseUtils().auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        findViewById<Button>(R.id.checkout).setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recycler_view_cart)

        auth.currentUser?.let {
            firestore.collection("carts").document(it.uid).addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    val items = snapshot.toObject(Cart::class.java)?.items
                    Log.d("items", items.toString())
                    if (items != null) {
                        if (items.size > 0) {
                            cartItemArrayList = ArrayList(items?.values)
                            itemAdapter = CartItemAdapter(cartItemArrayList) { cartItem -> clickListener(cartItem) }
                            recyclerView.adapter = itemAdapter
                        }
                    }
                }
            }
        }
    }

    private fun clickListener(cartItem: CartItem) {
        Log.d("cartItem", cartItem.toString() )
    }
}