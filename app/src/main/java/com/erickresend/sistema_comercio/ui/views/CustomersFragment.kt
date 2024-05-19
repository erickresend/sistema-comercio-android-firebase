package com.erickresend.sistema_comercio.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erickresend.sistema_comercio.R
import com.erickresend.sistema_comercio.data.models.CustomerModel
import com.erickresend.sistema_comercio.databinding.CustomersFragmentBinding
import com.erickresend.sistema_comercio.ui.adapters.CustomerAdapter
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class CustomersFragment : Fragment(), CustomerAdapter.OnItemClick {

    private lateinit var _binding: CustomersFragmentBinding
    private lateinit var customerList: ArrayList<CustomerModel>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: CustomerAdapter
    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomersFragmentBinding.inflate(inflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = _binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)
        searchView = _binding.searchView
        recyclerView.setHasFixedSize(true)

        customerList = arrayListOf()

        adapter = CustomerAdapter(customerList, this)

        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        db.collection("customers").get()
            .addOnSuccessListener {
                for (document in it) {
                    customerList.add(document.toObject(CustomerModel::class.java))
                }
                adapter.notifyDataSetChanged()
            }.addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()

        _binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_customersFragment_to_insertCustomerFragment)
        }
    }

    private fun filterList(query : String?) {
        if(query != null) {
            val filteredList = ArrayList<CustomerModel>()
            for(i in customerList) {
                if(i.name?.lowercase(Locale.ROOT)?.contains(query) == true) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(context, "Cliente não encontrado", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    override fun onClick(customer: CustomerModel) {

        val action = CustomersFragmentDirections.actionCustomersFragmentToEditCustomerFragment(customer)
        findNavController().navigate(action)
    }
}