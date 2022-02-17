package com.example.hatshop.admin

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

class AdminActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var hatArrayList : ArrayList<Hat>
    private lateinit var hatAdapter: HatAdapter
    private lateinit var logoutBtn: Button
    private val firestore = FirebaseUtils().firestoreDatabase
    private val auth = FirebaseUtils().auth
    private lateinit var addHatBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        logoutBtn = findViewById(R.id.logout)
        logoutBtn.setOnClickListener {
        startActivity(intent)
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        addHatBtn = findViewById(R.id.add_hat)
        addHatBtn.setOnClickListener {
            val intent = Intent(this, AddHatActivity::class.java)
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

    private fun clickListener(hat: Hat) {

    }

    private fun EventChangeListener() {
        firestore.collection("hats").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("error", error.message.toString())
                }

                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        hatArrayList.add(dc.document.toObject(Hat::class.java))
                    }
                }

                hatAdapter.notifyDataSetChanged()
            }
        })
    }
}