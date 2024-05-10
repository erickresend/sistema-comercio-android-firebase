package com.erickresend.sistema_comercio.ui.views

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erickresend.sistema_comercio.databinding.InsertProductFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class InsertProductFragment : Fragment() {

    private lateinit var _binding: InsertProductFragmentBinding
    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InsertProductFragmentBinding.inflate(inflater)
        return _binding.root
    }

    override fun onStart() {
        super.onStart()

        _binding.btnSave.setOnClickListener {
            insertProduct(it)
        }
    }

    private fun insertProduct(view: View) {
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