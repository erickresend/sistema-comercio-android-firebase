package com.erickresend.sistema_comercio.ui.views

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.text.set
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.erickresend.sistema_comercio.R
import com.erickresend.sistema_comercio.databinding.OpenPointOfSaleFragmentBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.logging.SimpleFormatter

class OpenPointOfSaleFragment: Fragment() {

    private lateinit var _binding: OpenPointOfSaleFragmentBinding
    private val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    val currentDate: LocalDate = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    @RequiresApi(Build.VERSION_CODES.O)
    val formattedDate: String = currentDate.format(formatter)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = OpenPointOfSaleFragmentBinding.inflate(inflater)
        return _binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.textCurrentDate.text = formattedDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()

        _binding.btnOpenPointOfSale.setOnClickListener {

            val change = _binding.editChange.text.toString().toDouble()

            val posMap = hashMapOf(
                "change" to change,
                "money" to 0.0,
                "pix" to 0.0,
                "credit card" to 0.0,
                "debit card" to 0.0,
                "totalSaleDay" to 0.0
            )

            db.collection("pointOfSale").document(formattedDate)
                .set(posMap).addOnCompleteListener {
                    Toast.makeText(context, "Abriu o caixa", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {

                }

            findNavController().navigate(R.id.action_openPointOfSaleFragment_to_pointOfSaleFragment)
        }

        _binding.btnHome.setOnClickListener {
            findNavController().navigate(R.id.action_openPointOfSaleFragment_to_homeFragment)
        }
    }
}