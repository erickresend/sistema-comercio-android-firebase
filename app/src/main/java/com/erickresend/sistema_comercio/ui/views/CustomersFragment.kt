package com.erickresend.sistema_comercio.ui.views

import android.app.Dialog
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
import com.google.firebase.firestore.Query

class CustomersFragment : Fragment(), CustomerAdapter.OnItemClick,
    CustomerAdapter.LastCustomerShownRecyclerview {

    private lateinit var _binding: CustomersFragmentBinding
    private lateinit var customerList: ArrayList<CustomerModel>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: CustomerAdapter
    private var db = FirebaseFirestore.getInstance()

    private lateinit var nextQuery: Query
    private var searching = false

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

        adapter = CustomerAdapter(customerList, this, this)

        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searching = true
                searchName(newText.toString())
                return true
            }
        })
    }

    override fun onStart() {
        super.onStart()

        showCustomersDB()

        _binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_customersFragment_to_insertCustomerFragment)
        }
    }

    override fun onClick(customer: CustomerModel) {

        val action = CustomersFragmentDirections.actionCustomersFragmentToEditCustomerFragment(customer)
        findNavController().navigate(action)
    }

    override fun lastCustomerShownRecyclerview(isShow: Boolean) {
        if (!searching) {
            showMoreCustomersDB()
        }
    }

    private fun showCustomersDB() {
        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.show()

        customerList.clear()

        db.collection("customers").orderBy("name").limit(8)
            .get().addOnSuccessListener { documents ->

                dialog?.dismiss()

                if (documents.size() > 0) {
                    val lastDocument = documents.documents[documents.size() - 1]
                    nextQuery = db.collection("customers").orderBy("name").startAfter(lastDocument).limit(3)
                    for (document in documents) {
                        customerList.add(document.toObject(CustomerModel::class.java))
                    }
                    adapter.notifyDataSetChanged()
                }
            }.addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun showMoreCustomersDB() {
        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.show()

        nextQuery.get().addOnSuccessListener { documents ->

            dialog?.dismiss()

            if (documents.size() > 0) {
                val lastDocument = documents.documents[documents.size() - 1]
                nextQuery = db.collection("customers").orderBy("name").startAfter(lastDocument).limit(8)
                for (document in documents) {
                    customerList.add(document.toObject(CustomerModel::class.java))
                }
                adapter.notifyDataSetChanged()
            }
        }.addOnFailureListener {
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchName(newText: String) {

        db.collection("customers").orderBy("name").startAt(newText)
            .endAt(newText+"\uf8ff").limit(8)
            .get().addOnSuccessListener { documents ->

                if (newText == "") {
                    searching = false
                    showCustomersDB()
                } else if (documents.size() > 0) {

                    customerList.clear()

                    for (document in documents) {
                        customerList.add(document.toObject(CustomerModel::class.java))
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    customerList.clear()
                    adapter.notifyDataSetChanged()
                }
            }
    }
}