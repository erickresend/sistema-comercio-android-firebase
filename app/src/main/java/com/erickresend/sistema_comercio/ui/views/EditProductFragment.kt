package com.erickresend.sistema_comercio.ui.views

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.erickresend.sistema_comercio.databinding.EditProductFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class EditProductFragment : Fragment() {

    private lateinit var _binding: EditProductFragmentBinding
    private var db = FirebaseFirestore.getInstance()

    private val args : EditProductFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditProductFragmentBinding.inflate(inflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.editProductName.setText(args.product.name)
        _binding.editProductPrice.setText(args.product.price.toString())
    }

    override fun onStart() {
        super.onStart()

        _binding.btnEdit.setOnClickListener { view ->

            val productName = _binding.editProductName.text.toString()
            val productPrice = _binding.editProductPrice.text.toString()

            args.product.documentId?.let {
                db.collection("products").document(it)
                    .update("name", productName, "price", productPrice).addOnCompleteListener {
                        val snackbar = Snackbar.make(view, "Produto alterado com sucesso", Snackbar.LENGTH_SHORT)
                        snackbar.setBackgroundTint(Color.GREEN)
                        snackbar.show()
                    }.addOnFailureListener {

                    }
            }
        }
    }
}