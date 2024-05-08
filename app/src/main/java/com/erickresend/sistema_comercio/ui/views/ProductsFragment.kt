package com.erickresend.sistema_comercio.ui.views

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erickresend.sistema_comercio.data.models.ProductModel
import com.erickresend.sistema_comercio.databinding.ProductsFragmentBinding
import com.erickresend.sistema_comercio.ui.adapters.ProductAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import java.util.Locale

class ProductsFragment : Fragment() {

    private lateinit var _binding: ProductsFragmentBinding
    private lateinit var productList: ArrayList<ProductModel>
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: ProductAdapter
    private lateinit var db: FirebaseFirestore

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
        searchView = _binding.searchView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        productList = arrayListOf()

        adapter = ProductAdapter(productList)

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

        db = FirebaseFirestore.getInstance()

        db.collection("products")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if(error != null) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
                        return
                    }

                    for(dc : DocumentChange in  value?.documentChanges!!) {
                        if(dc.type == DocumentChange.Type.ADDED) {
                            productList.add(dc.document.toObject(ProductModel::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })
    }

    override fun onStart() {
        super.onStart()

        _binding.btnSave.setOnClickListener { view ->

            val productName = _binding.editProductName.text.toString()
            val productPrice = _binding.editProductPrice.text.toString()

            val productsMap = hashMapOf(
                "name" to productName,
                "price" to productPrice.toDouble()
            )

            db.collection("products").document()
                .set(productsMap).addOnCompleteListener {
                    val snackbar = Snackbar.make(view, "Produto salvo com sucesso", Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.GREEN)
                    snackbar.show()
                }.addOnFailureListener {

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
                Toast.makeText(context, "Produto não encontrado", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }
}