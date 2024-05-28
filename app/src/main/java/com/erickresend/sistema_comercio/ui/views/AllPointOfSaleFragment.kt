package com.erickresend.sistema_comercio.ui.views

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erickresend.sistema_comercio.R
import com.erickresend.sistema_comercio.data.models.PointOfSaleModel
import com.erickresend.sistema_comercio.databinding.AllPointOfSaleFragmentBinding
import com.erickresend.sistema_comercio.ui.adapters.PointOfSaleAdapter
import com.google.firebase.firestore.FirebaseFirestore

class AllPointOfSaleFragment : Fragment() {

    private lateinit var _binding: AllPointOfSaleFragmentBinding
    private lateinit var pointOfSaleList: ArrayList<PointOfSaleModel>
    private lateinit var adapter: PointOfSaleAdapter
    private lateinit var recyclerView: RecyclerView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AllPointOfSaleFragmentBinding.inflate(inflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pointOfSaleList = arrayListOf()
        adapter = PointOfSaleAdapter(pointOfSaleList)
        _binding.recyclerview.adapter = adapter

        val dialog = context?.let { Dialog(it) }
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.show()

        db.collection("pointOfSale").get()
            .addOnSuccessListener {

                dialog?.dismiss()

                for (document in it) {
                    pointOfSaleList.add(document.toObject(PointOfSaleModel::class.java))
                }
                adapter.notifyDataSetChanged()
            }.addOnFailureListener {
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}