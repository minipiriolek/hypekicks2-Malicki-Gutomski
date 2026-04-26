package com.example.hypekicks2_malicki_gutomski

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.hypekicks2_malicki_gutomski.databinding.ActivityStorefrontBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class StorefrontActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStorefrontBinding
    private val db = Firebase.firestore

    private lateinit var adapter: SneakerAdapter
    private var allSneakers = mutableListOf<Sneaker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorefrontBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = SneakerAdapter(this, allSneakers)
        binding.gridView.adapter = adapter

        fetchSneakers()

        binding.btnGoToAdmin.setOnClickListener {
            startActivity(Intent(this, AdminPanelActivity::class.java))
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        binding.gridView.setOnItemClickListener { _, _, position, _ ->
            val selectedSneaker = if (binding.searchView.query.isEmpty()) {
                allSneakers[position]
            } else {
                val filtered = allSneakers.filter { it.modelName.contains(binding.searchView.query, ignoreCase = true) }
                filtered[position]
            }

            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("sneaker", selectedSneaker)
            startActivity(intent)
        }
    }

    private fun fetchSneakers() {
        db.collection("sneakers").addSnapshotListener { snapshots, e ->
            if (e != null) return@addSnapshotListener

            allSneakers.clear()
            for (doc in snapshots!!) {
                val sneaker = doc.toObject(Sneaker::class.java)
                sneaker.id = doc.id
                allSneakers.add(sneaker)
            }
            adapter.updateList(allSneakers)
        }
    }

    private fun filterList(query: String?) {
        val filtered = if (query.isNullOrEmpty()) {
            allSneakers
        } else {
            allSneakers.filter { it.modelName.contains(query, ignoreCase = true) }
        }
        adapter.updateList(filtered)
    }

    override fun onResume() {
        super.onResume()
        binding.searchView.setQuery("", false)
        binding.searchView.clearFocus()
    }
}