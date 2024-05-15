package com.erickresend.sistema_comercio.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erickresend.sistema_comercio.data.models.ProductAddedModel
import com.erickresend.sistema_comercio.data.models.ProductModel
import com.erickresend.sistema_comercio.databinding.ResProductAddedBinding

class ProductAddedAdapter(
    private var productsListAdded: ArrayList<ProductAddedModel>,
    var onItemClick: OnItemClick
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

    interface OnItemClick {
        fun onClick(product: ProductAddedModel)
    }

    inner class ProductAddedViewHolder(private val binding: ResProductAddedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductAddedModel) {
            binding.textProductDescription.text = product.name
            binding.textProductPrice.text = product.price.toString()
            binding.textProductQuantity.text = product.quantity.toString()
            binding.btnDeleteProduct.setOnClickListener {
                onItemClick.onClick(product)
            }
        }
    }
}