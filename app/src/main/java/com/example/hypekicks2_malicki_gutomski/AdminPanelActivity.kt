package com.example.hypekicks2_malicki_gutomski

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hypekicks2_malicki_gutomski.databinding.ActivityAdminPanelBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminPanelBinding
    private val db = Firebase.firestore

    private lateinit var adapter: ArrayAdapter<String>
    private val sneakerList = mutableListOf<Sneaker>()
    private val displayList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayList)
        binding.lvSneakers.adapter = adapter

        fetchSneakers()

        binding.btnAdd.setOnClickListener {
            val brand = binding.etBrand.text.toString()
            val model = binding.etModel.text.toString()
            val year = binding.etYear.text.toString().toIntOrNull() ?: 0
            val price = binding.etPrice.text.toString().toIntOrNull() ?: 0
            val url = binding.etImageUrl.text.toString()

            if (brand.isNotEmpty() && model.isNotEmpty()) {
                val newSneaker = Sneaker(brand, model, year, price, url)
                saveToFirebase(newSneaker)
            } else {
                Toast.makeText(this, "Wypełnij markę i model!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.lvSneakers.setOnItemLongClickListener { _, _, position, _ ->
            val sneakerToDelete = sneakerList[position]

            AlertDialog.Builder(this)
                .setTitle("Usuwanie z magazynu")
                .setMessage("Czy na pewno chcesz usunąć: ${sneakerToDelete.modelName}?")
                .setPositiveButton("Tak, usuń") { _, _ ->
                    deleteSneaker(sneakerToDelete.id)
                }
                .setNegativeButton("Anuluj", null)
                .show()

            true
        }
    }

    private fun saveToFirebase(sneaker: Sneaker) {
        db.collection("sneakers")
            .add(sneaker)
            .addOnSuccessListener {
                Toast.makeText(this, "Dodano pomyślnie!", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchSneakers() {
        db.collection("sneakers").addSnapshotListener { snapshots, e ->
            if (e != null) return@addSnapshotListener

            sneakerList.clear()
            displayList.clear()

            for (doc in snapshots!!) {
                val sneaker = doc.toObject(Sneaker::class.java)
                sneaker.id = doc.id
                sneakerList.add(sneaker)
                displayList.add("${sneaker.brand} ${sneaker.modelName} - ${sneaker.resellPrice} PLN")
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun deleteSneaker(id: String) {
        db.collection("sneakers").document(id).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "But usunięty!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Błąd usuwania: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        binding.etBrand.text.clear()
        binding.etModel.text.clear()
        binding.etPrice.text.clear()
        binding.etYear.text.clear()
        binding.etImageUrl.text.clear()
    }
}