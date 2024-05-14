package com.erickresend.sistema_comercio.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erickresend.sistema_comercio.data.models.ProductModel
import com.erickresend.sistema_comercio.databinding.ResProductAddedBinding

class ProductAddedAdapter(
    private var productsListAdded: ArrayList<ProductModel>
): RecyclerView.Adapter<ProductAddedAdapter.ProductAddedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAddedViewHolder {
        val binding = ResProductAddedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductAddedViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productsListAdded.size
    }

    override fun onBindViewHolder(holder: ProductAddedViewHolder, position: Int) {
        val product = productsListAdded[position]
        holder.bind(product)
    }

    inner class ProductAddedViewHolder(private val binding: ResProductAddedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductModel) {
            binding.textProductDescription.text = product.name
            binding.textProductPrice.text = product.price.toString()
        }
    }
}