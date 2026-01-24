package com.example.cafecornerapp.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafecornerapp.Domain.ProductModel
import com.example.cafecornerapp.databinding.ViewHolderCardproductBinding

class CardProductAdapter(val items: MutableList<ProductModel>):
    RecyclerView.Adapter<CardProductAdapter.Viewholder>() {

    lateinit var context: Context
    class Viewholder(val binding: ViewHolderCardproductBinding):
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardProductAdapter.Viewholder {
        context= parent.context
        val binding = ViewHolderCardproductBinding.
        inflate(LayoutInflater.from(context),parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: CardProductAdapter.Viewholder, position: Int) {
        holder.binding.titleTxt.text= items[position].nama_product
        holder.binding.priceTxt.text="$"+items[position].harga_product.toString()
        holder.binding.subtitleTxt.text= items[position].deskripsi_product.toString()

        Glide.with(context).load(items[position].imgUrl).into(holder.binding.pic)

        holder.itemView.setOnClickListener {
//            val intent = Intent(context, DetailActivity::class.java)
//            intent.putExtra("object", items[position])
//            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int =items.size
}