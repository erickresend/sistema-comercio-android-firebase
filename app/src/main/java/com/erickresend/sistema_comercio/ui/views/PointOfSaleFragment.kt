package com.erickresend.sistema_comercio.ui.views

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.erickresend.sistema_comercio.R
import com.erickresend.sistema_comercio.data.models.ProductAddedModel
import com.erickresend.sistema_comercio.databinding.PointOfSaleFragmentBinding
import com.erickresend.sistema_comercio.ui.adapters.ProductAddedAdapter
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PointOfSaleFragment : Fragment(), ProductAddedAdapter.OnItemClick {

    private lateinit var _binding: PointOfSaleFragmentBinding
    private lateinit var productListAdded: ArrayList<ProductAddedModel>
    private lateinit var adapter: ProductAddedAdapter
    private val db = FirebaseFirestore.getInstance()
    private var totalSale: Double = 0.0

    private lateinit var spinner: Spinner

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

        productListAdded = arrayListOf()
        adapter = ProductAddedAdapter(productListAdded, this)
        _binding.recyclerview.adapter = adapter

        spinner  = _binding.spinnerPayments
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.payments_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

        _binding.btnAddProduct.setOnClickListener {

            val productId = _binding.editProductId.text.toString()
            val productName = _binding.textProductName.text.toString()
            val productPrice = _binding.textProductPrice.text.toString().toDouble()
            val discount = _binding.editDiscount.text.toString().toDouble()

            val product = ProductAddedModel(productId, productName, productPrice, discount)
 
            productListAdded.add(product)
            adapter.notifyDataSetChanged()

            totalSale += productPrice  - discount
            _binding.textTotalSale.text = totalSale.toString()

            _binding.editProductId.setText("")
            _binding.editDiscount.setText("0")
        }

        _binding.btnEndSale.setOnClickListener {
            //val selectedPayment = spinner.selectedItem.toString()

            val currentDate: LocalDate = LocalDate.now()
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val formattedDate: String = currentDate.format(formatter)

            // Atualizar o valor vendido no dia
            db.collection("pointOfSale").document(formattedDate)
                .get().addOnSuccessListener {
                    val totalSaleDay = it.getDouble("totalSaleDay")
                    if (totalSaleDay != null) {
                        db.collection("pointOfSale").document(formattedDate)
                            .update("totalSaleDay", (totalSaleDay + totalSale))
                            .addOnSuccessListener {
                                Toast.makeText(context, "Venda salva com sucesso", Toast.LENGTH_SHORT).show()
                            }
                    }
                }

            for (product: ProductAddedModel in productListAdded) {
                product.productId?.let { it1 ->
                    db.collection("products").document(it1)
                        .delete()
                }
            }
        }
    }

    override fun onClick(product: ProductAddedModel) {
        productListAdded.remove(product)
        adapter.notifyDataSetChanged()

        val valueRemoved = product.price

        if (valueRemoved != null) {
            totalSale -= valueRemoved
            _binding.textTotalSale.text = totalSale.toString()
        }
    }
}