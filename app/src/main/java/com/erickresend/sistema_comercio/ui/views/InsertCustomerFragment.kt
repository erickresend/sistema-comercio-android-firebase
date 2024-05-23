package com.erickresend.sistema_comercio.ui.views

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.erickresend.sistema_comercio.R
import com.erickresend.sistema_comercio.databinding.InsertCustomerFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class InsertCustomerFragment : Fragment() {

    private lateinit var _binding : InsertCustomerFragmentBinding
    private var db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = InsertCustomerFragmentBinding.inflate(inflater)
        return _binding.root
    }

    override fun onStart() {
        super.onStart()

        _binding.btnSave.setOnClickListener { view ->

            val customerName = _binding.editName.text.toString()
            val customerCpf = _binding.editCpf.masked
            val customerPhone = _binding.editPhone.masked
            val customerBirthDate = _binding.editBirthDate.masked
            val customerAddress = _binding.editAddress.text.toString()

            if (customerName.isBlank() || customerName.isBlank() ||
                !_binding.editCpf.isDone || !_binding.editPhone.isDone ||
                !_binding.editBirthDate.isDone || customerAddress.isBlank() ||
                customerAddress.isBlank()) {
                val snackbar = Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                val customersMap = hashMapOf(
                    "name" to customerName,
                    "cpf" to customerCpf,
                    "phone" to customerPhone,
                    "birthDate" to customerBirthDate,
                    "address" to customerAddress
                )

                db.collection("customers").document()
                    .set(customersMap).addOnCompleteListener {
                        val snackbar = Snackbar.make(view, "Cliente inserido com sucesso", Snackbar.LENGTH_SHORT)
                        snackbar.setBackgroundTint(Color.GREEN)
                        snackbar.show()
                    }.addOnFailureListener {

                    }
            }
        }
    }
}