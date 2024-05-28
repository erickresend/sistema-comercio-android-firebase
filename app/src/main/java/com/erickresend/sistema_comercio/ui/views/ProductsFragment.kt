package com.erickresend.sistema_comercio.ui.views

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import java.time.LocalDate
import java.util.Locale

class ProductsFragment : Fragment(), ProductAdapter.OnItemClick {

    private lateinit var _binding: ProductsFragmentBinding
    private lateinit var productList: ArrayList<ProductModel>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: ProductAdapter
    private var db = FirebaseFirestore.getInstance()

    private lateinit var nextQuery: Query

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

        adapter = ProductAdapter(productList, this)

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

        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.show()

        db.collection("products").orderBy("name").limit(3)
            .get().addOnSuccessListener { documents ->

                dialog?.dismiss()

                if (documents.size() > 0) {
                    val lastDocument = documents.documents[documents.size() - 1]
                    nextQuery = db.collection("products").orderBy("name").startAfter(lastDocument).limit(3)
                    for (document in documents) {
                        productList.add(document.toObject(ProductModel::class.java))
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    _binding.btnShowMore.visibility = View.INVISIBLE
                }
            }.addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()

        _binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_insertProductFragment)
        }

        _binding.btnShowMore.setOnClickListener {
            val dialog = context?.let { Dialog(it) }
            dialog?.setContentView(R.layout.dialog_loading)
            dialog?.show()

            nextQuery.get().addOnSuccessListener { documents ->

                dialog?.dismiss()

                if (documents.size() > 0) {
                    val lastDocument = documents.documents[documents.size() - 1]
                    nextQuery = db.collection("products").orderBy("name").startAfter(lastDocument).limit(3)
                    for (document in documents) {
                        productList.add(document.toObject(ProductModel::class.java))
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    _binding.btnShowMore.visibility = View.INVISIBLE
                }
            }.addOnFailureListener {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun filterList(query : String?) {
        if(query != null) {
            val filteredList = ArrayList<ProductModel>()
            for(i in productList) {
                if(i.name?.lowercase(Locale.ROOT)?.contains(query) == true) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                //Toast.makeText(context, "Produto não encontrado", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    override fun onClick(product: ProductModel) {

        val action = ProductsFragmentDirections.actionProductsFragmentToEditProductFragment(product)
        findNavController().navigate(action)
    }
}
