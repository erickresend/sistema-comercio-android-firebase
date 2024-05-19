package com.erickresend.sistema_comercio.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erickresend.sistema_comercio.data.models.CustomerModel
import com.erickresend.sistema_comercio.databinding.ResCustomerBinding

class CustomerAdapter(
    private var customersList: ArrayList<CustomerModel>,
    var onItemClick: OnItemClick
) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val binding = ResCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomerViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return customersList.size
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer = customersList[position]
        holder.bind(customer)
    }

    interface OnItemClick {
        fun onClick(customer: CustomerModel)
    }

    inner class CustomerViewHolder(private val binding: ResCustomerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(customer: CustomerModel) {
            binding.textName.text = customer.name
            binding.textCpf.text = customer.cpf.toString()
            binding.textPhone.text = customer.phone.toString()
            binding.textBirthDate.text = customer.birthDate
            binding.textAddress.text = customer.address
            binding.cardResCustomer.setOnClickListener {
                onItemClick.onClick(customer)
            }
        }
    }

    fun setFilteredList(customersList: ArrayList<CustomerModel>) {
        this.customersList = customersList
        notifyDataSetChanged()
    }
}