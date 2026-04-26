package com.example.hypekicks2_malicki_gutomski

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.example.hypekicks2_malicki_gutomski.databinding.ItemSneakerBinding

class SneakerAdapter(private val context: Context, private var list: List<Sneaker>) : BaseAdapter() {

    override fun getCount(): Int = list.size
    override fun getItem(position: Int): Any = list[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ItemSneakerBinding
        val view: View

        if (convertView == null) {
            binding = ItemSneakerBinding.inflate(LayoutInflater.from(context), parent, false)
            view = binding.root
            view.tag = binding
        } else {
            binding = convertView.tag as ItemSneakerBinding
            view = convertView
        }

        val sneaker = list[position]
        binding.tvBrandName.text = sneaker.brand
        binding.tvModelName.text = sneaker.modelName


        // Magia Glide - pobieranie zdjęcia z linku
        Glide.with(context)
            .load(sneaker.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery) // obrazek zastępczy
            .into(binding.ivSneakerImage)

        return view
    }

    fun updateList(newList: List<Sneaker>) {
        list = newList
        notifyDataSetChanged()
    }
}