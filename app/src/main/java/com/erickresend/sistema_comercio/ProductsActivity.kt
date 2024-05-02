package com.erickresend.sistema_comercio

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erickresend.sistema_comercio.databinding.ActivityProductsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class ProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductsBinding
    private lateinit var productList: ArrayList<ProductModel>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var db:  FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        productList = arrayListOf()

        adapter = ProductAdapter(productList)

        recyclerView.adapter = adapter

        /*db.collection("products").get()
            .addOnSuccessListener {
                if(!it.isEmpty) {
                    for(data in it.documents) {
                        val product: ProductModel? = data.toObject(ProductModel::class.java)
                        if (product != null) {
                            productList.add(product)
                        }
                    }
                    recyclerView.adapter = ProductAdapter(productList)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }*/

        db = FirebaseFirestore.getInstance()

        db.collection("products")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if(error != null) {
                        Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
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

        binding.btnSave.setOnClickListener { view ->

            val productName = binding.editProductName.text.toString()
            val productPrice = binding.editProductPrice.text.toString()

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
}