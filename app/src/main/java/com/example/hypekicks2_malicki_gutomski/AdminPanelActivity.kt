package com.example.hypekicks2_malicki_gutomski

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hypekicks2_malicki_gutomski.databinding.ActivityAdminPanelBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminPanelBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                Toast.makeText(this, "Wypełnij chociaż markę i model!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveToFirebase(sneaker: Sneaker) {
        db.collection("sneakers")
            .add(sneaker)
            .addOnSuccessListener {
                Toast.makeText(this, "Buty dodane do magazynu!", Toast.LENGTH_SHORT).show()
                // Czyścimy pola po dodaniu
                binding.etBrand.text.clear()
                binding.etModel.text.clear()
                binding.etPrice.text.clear()
                binding.etYear.text.clear()
                binding.etImageUrl.text.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Błąd: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}