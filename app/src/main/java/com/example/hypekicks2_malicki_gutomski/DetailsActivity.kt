package com.example.hypekicks2_malicki_gutomski

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hypekicks2_malicki_gutomski.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sneaker = intent.getSerializableExtra("sneaker") as? Sneaker

        if (sneaker != null) {
            binding.tvDetailBrand.text = sneaker.brand
            binding.tvDetailModel.text = sneaker.modelName
            binding.tvDetailPrice.text = "${sneaker.resellPrice} PLN"
            binding.tvDetailYear.text = "Rok wydania: ${sneaker.releaseYear}"

            Glide.with(this)
                .load(sneaker.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(binding.ivLargeImage)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}