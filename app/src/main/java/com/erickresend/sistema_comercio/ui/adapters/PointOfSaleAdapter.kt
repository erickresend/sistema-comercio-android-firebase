package com.erickresend.sistema_comercio.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erickresend.sistema_comercio.data.models.PointOfSaleModel
import com.erickresend.sistema_comercio.databinding.ResPointOfSaleBinding

class PointOfSaleAdapter(
    private var pointOfSaleList: ArrayList<PointOfSaleModel>
) : RecyclerView.Adapter<PointOfSaleAdapter.PointOfSaleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointOfSaleViewHolder {
        val binding = ResPointOfSaleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PointOfSaleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return pointOfSaleList.size
    }

    override fun onBindViewHolder(holder: PointOfSaleViewHolder, position: Int) {
        val pointOfSale = pointOfSaleList[position]
        holder.bind(pointOfSale)
    }

    inner class PointOfSaleViewHolder(private val binding: ResPointOfSaleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pointOfSale: PointOfSaleModel) {
            binding.textDate.text = pointOfSale.documentId
            binding.textChange.text = pointOfSale.change.toString()
            binding.textTotalSaleDay.text = pointOfSale.totalSaleDay.toString()
        }
    }
}