package com.example.hatshop.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hatshop.FirebaseUtils
import com.example.hatshop.LoginActivity
import com.example.hatshop.R
import com.example.hatshop.adapter.HatAdapter
import com.example.hatshop.model.Hat
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class ShopActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var hatArrayList : ArrayList<Hat>
    private lateinit var hatAdapter: HatAdapter
    private lateinit var logoutBtn: Button
    private val firestore = FirebaseUtils().firestoreDatabase
    private val auth = FirebaseUtils().auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)

        val cartBtn = findViewById<Button>(R.id.my_cart)
        cartBtn.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        logoutBtn = findViewById(R.id.logout)
        logoutBtn.setOnClickListener {
            auth.signOut()
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        hatArrayList = arrayListOf()
        hatAdapter = HatAdapter(hatArrayList) {
            hat -> clickListener(hat)
        }
        recyclerView.adapter = hatAdapter

        EventChangeListener()

    }

    fun clickListener(hat: Hat) {
        val intent = Intent(this, HatDetailsActivity::class.java)
        intent.putExtra("id", hat.id)
        intent.putExtra("name", hat.name)
        intent.putExtra("price", hat.price)
        intent.putExtra("stock", hat.stock)
        intent.putExtra("downloadURL", hat.downloadURL)
        startActivity(intent)
    }

    private fun EventChangeListener() {
        firestore.collection("hats").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("error", error.message.toString())
                }

                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        val data = dc.document.toObject(Hat::class.java)
                        data.id = dc.document.id
                        hatArrayList.add(data)
                    }
                }

                hatAdapter.notifyDataSetChanged()
            }
        })
    }
}