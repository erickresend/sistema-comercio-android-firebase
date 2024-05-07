package com.erickresend.sistema_comercio.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erickresend.sistema_comercio.databinding.ProductsFragmentBinding

class ProductsFragment : Fragment() {

    private lateinit var _binding: ProductsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProductsFragmentBinding.inflate(inflater)
        return _binding.root
    }
}