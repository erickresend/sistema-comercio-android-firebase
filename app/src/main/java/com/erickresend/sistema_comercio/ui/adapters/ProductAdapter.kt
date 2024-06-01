package com.erickresend.sistema_comercio.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erickresend.sistema_comercio.data.models.ProductModel
import com.erickresend.sistema_comercio.databinding.ResProductBinding


class ProductAdapter(
    private var productsList: ArrayList<ProductModel>,
    var onItemClick: OnItemClick,
    private var lastItemShownRecyclerview: LastItemShownRecyclerview
): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ResProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productsList[position]
        holder.bind(product)

        if (position == itemCount - 1) {
            lastItemShownRecyclerview.lastItemShownRecyclerview(true)
        }
    }

    interface OnItemClick {
        fun onClick(product: ProductModel)
    }

    interface LastItemShownRecyclerview {

        fun lastItemShownRecyclerview(isShow: Boolean)
    }

    inner class ProductViewHolder(private val binding: ResProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductModel) {
            binding.textName.text = product.name
            binding.textPrice.text = product.price.toString()
            binding.textId.text = product.documentId
            binding.cardResProduct.setOnClickListener {
                onItemClick.onClick(product)
            }
        }
    }
}