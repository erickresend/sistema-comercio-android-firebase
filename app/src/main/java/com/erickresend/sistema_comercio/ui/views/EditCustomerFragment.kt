package com.erickresend.sistema_comercio.ui.views

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.erickresend.sistema_comercio.R
import com.erickresend.sistema_comercio.databinding.EditCustomerFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class EditCustomerFragment : Fragment() {

    private lateinit var _binding: EditCustomerFragmentBinding
    private var db = FirebaseFirestore.getInstance()
    private val args : EditCustomerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditCustomerFragmentBinding.inflate(inflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.editName.setText(args.customer.name)
        _binding.editCpf.setText(args.customer.cpf)
        _binding.editPhone.setText(args.customer.phone)
        _binding.editBirthDate.setText(args.customer.birthDate)
        _binding.editAddress.setText(args.customer.address)
    }

    override fun onStart() {
        super.onStart()

        _binding.btnEdit.setOnClickListener { view ->

            val customerName = _binding.editName.text.toString()
            val customerCpf = _binding.editCpf.masked
            val customerPhone = _binding.editPhone.masked
            val customerBirthDate = _binding.editBirthDate.masked
            val customerAddress = _binding.editAddress.text.toString()

            if (customerName.isBlank() || customerName.isBlank() ||
                customerAddress.isBlank() || customerAddress.isBlank()) {
                val snackbar = Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            } else {
                args.customer.documentId?.let {documentId ->
                    db.collection("customers").document(documentId)
                        .update(
                            "name", customerName,
                            "cpf", customerCpf,
                            "phone", customerPhone,
                            "birthDate", customerBirthDate,
                            "address", customerAddress
                        ).addOnCompleteListener {
                            val snackbar = Snackbar.make(view, "Cliente alterado com sucesso", Snackbar.LENGTH_SHORT)
                            snackbar.setBackgroundTint(Color.GREEN)
                            snackbar.show()
                            activity?.onBackPressedDispatcher?.onBackPressed()
                        }.addOnFailureListener {

                        }
                }
            }
        }

        _binding.btnDeleteCustomer.setOnClickListener { view ->
            args.customer.documentId?.let {documentId ->
                db.collection("customers").document(documentId).delete()
                    .addOnCompleteListener {
                        val snackbar = Snackbar.make(view, "Cliente deletado com sucesso", Snackbar.LENGTH_SHORT)
                        snackbar.setBackgroundTint(Color.GREEN)
                        snackbar.show()
                        activity?.onBackPressedDispatcher?.onBackPressed()
                    }.addOnFailureListener {

                    }
            }
        }
    }
}