package com.erickresend.sistema_comercio.ui.views

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erickresend.sistema_comercio.R
import com.erickresend.sistema_comercio.data.models.ProductModel
import com.erickresend.sistema_comercio.databinding.PointOfSaleFragmentBinding
import com.erickresend.sistema_comercio.ui.adapters.ProductAdapter
import com.erickresend.sistema_comercio.ui.adapters.ProductAddedAdapter
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PointOfSaleFragment : Fragment() {

    private lateinit var _binding: PointOfSaleFragmentBinding
    //private lateinit var productListAdded: ArrayList<ProductModel>
    //private lateinit var recyclerView: RecyclerView
    //private lateinit var adapter: ProductAddedAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PointOfSaleFragmentBinding.inflate(inflater)
        return _binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDate: LocalDate = LocalDate.now()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formattedDate: String = currentDate.format(formatter)

        db.collection("pointOfSale").document(formattedDate)
            .get()
            .addOnSuccessListener {
                if (!it.exists()) {
                    findNavController().navigate(R.id.action_pointOfSaleFragment_to_openPointOfSaleFragment)
                }
            }
            .addOnFailureListener {

            }

        /*recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        productListAdded = arrayListOf()
        adapter = ProductAddedAdapter(productListAdded)
        recyclerView.adapter = adapter*/
    }

    override fun onStart() {
        super.onStart()

        _binding.btnSearchProduct.setOnClickListener {
            val productId = _binding.editProductId.text.toString()
            db.collection("products").document(productId)
                .addSnapshotListener { value, error ->
                    if (value != null) {
                        _binding.textProductName.text = value.getString("name")
                        _binding.textProductPrice.text = value.getDouble("price").toString()
                    }
                }
        }

        /*_binding.btnAddProduct.setOnClickListener {

            val productId = _binding.editProductId.text.toString()
            val productName = _binding.textProductName.text.toString()
            val productPrice = _binding.textProductPrice.text.toString().toDouble()

            val product = ProductModel(productId, productName, productPrice)
 
            productListAdded.add(product)
            adapter.notifyDataSetChanged()
        }*/

    }
}