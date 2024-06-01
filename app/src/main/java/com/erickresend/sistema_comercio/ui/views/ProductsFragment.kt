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
import com.erickresend.sistema_comercio.data.models.ProductModel
import com.erickresend.sistema_comercio.databinding.ProductsFragmentBinding
import com.erickresend.sistema_comercio.ui.adapters.ProductAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ProductsFragment : Fragment(), ProductAdapter.OnItemClick,
    ProductAdapter.LastItemShownRecyclerview {

    private lateinit var _binding: ProductsFragmentBinding
    private lateinit var productList: ArrayList<ProductModel>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: ProductAdapter
    private var db = FirebaseFirestore.getInstance()

    private lateinit var nextQuery: Query
    private var searching = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductsFragmentBinding.inflate(inflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = _binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)
        searchView = _binding.searchView
        recyclerView.setHasFixedSize(true)

        productList = arrayListOf()

        adapter = ProductAdapter(productList, this, this)

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

        showItensDB()
    }

    override fun onStart() {
        super.onStart()

        _binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_insertProductFragment)
        }
    }

    override fun onClick(product: ProductModel) {

        val action = ProductsFragmentDirections.actionProductsFragmentToEditProductFragment(product)
        findNavController().navigate(action)
    }

    override fun lastItemShownRecyclerview(isShow: Boolean) {
        if (!searching) {
            showMoreItensDB()
        }
    }

    private fun showItensDB() {
        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.show()

        productList.clear()

        db.collection("products").orderBy("name").limit(8)
            .get().addOnSuccessListener { documents ->

                dialog?.dismiss()

                if (documents.size() > 0) {
                    val lastDocument = documents.documents[documents.size() - 1]
                    nextQuery = db.collection("products").orderBy("name").startAfter(lastDocument).limit(8)
                    for (document in documents) {
                        productList.add(document.toObject(ProductModel::class.java))
                    }
                    adapter.notifyDataSetChanged()
                }
            }.addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun showMoreItensDB() {
        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.show()

        nextQuery.get().addOnSuccessListener { documents ->

            dialog?.dismiss()

            if (documents.size() > 0) {
                val lastDocument = documents.documents[documents.size() - 1]
                nextQuery = db.collection("products").orderBy("name").startAfter(lastDocument).limit(8)
                for (document in documents) {
                    productList.add(document.toObject(ProductModel::class.java))
                }
                adapter.notifyDataSetChanged()
            }
        }.addOnFailureListener {
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchName(newText: String) {

        db.collection("products").orderBy("name").startAt(newText)
            .endAt(newText+"\uf8ff").limit(8)
            .get().addOnSuccessListener { documents ->

                if (newText == "") {
                    searching = false
                    showItensDB()
                } else if (documents.size() > 0) {

                    productList.clear()

                    for (document in documents) {
                        productList.add(document.toObject(ProductModel::class.java))
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    productList.clear()
                    adapter.notifyDataSetChanged()
                }
            }
    }
}