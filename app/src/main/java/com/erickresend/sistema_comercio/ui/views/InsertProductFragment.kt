package com.erickresend.sistema_comercio.ui.views

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.erickresend.sistema_comercio.R
import com.erickresend.sistema_comercio.databinding.InsertProductFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class InsertProductFragment : Fragment() {

    private lateinit var _binding: InsertProductFragmentBinding
    private var db = FirebaseFirestore.getInstance()
    private var randomNumber = (10000 until 100000).random()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InsertProductFragmentBinding.inflate(inflater)
        return _binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()

        _binding.btnSave.setOnClickListener {
            insertProduct(it)
        }

        _binding.btnNewProduct.setOnClickListener {
            findNavController().navigate(R.id.action_insertProductFragment_self)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertProduct(view: View) {

        val productName = _binding.editProductName.text.toString()
        val productPrice = _binding.editProductPrice.text.toString()

        if (productName.isEmpty() || productName.isBlank() ||
            productPrice.isEmpty() || productPrice.isBlank()) {
            val snackbar = Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
            snackbar.setBackgroundTint(Color.RED)
            snackbar.show()
        } else {
            val productsMap = hashMapOf(
                "name" to productName,
                "price" to productPrice.toDouble()
            )
            db.collection("products").document(randomNumber.toString())
                .set(productsMap).addOnCompleteListener {
                    val snackbar = Snackbar.make(view, "Produto inserido com sucesso", Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.GREEN)
                    snackbar.show()
                }
            _binding.textProductId.text = randomNumber.toString()
            _binding.editProductName.focusable = View.NOT_FOCUSABLE
            _binding.editProductPrice.focusable = View.NOT_FOCUSABLE
            _binding.btnSave.visibility = View.INVISIBLE
            _binding.textTitleProductId.visibility = View.VISIBLE
        }
    }
}