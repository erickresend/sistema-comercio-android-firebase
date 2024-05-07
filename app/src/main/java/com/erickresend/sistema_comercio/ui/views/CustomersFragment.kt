package com.erickresend.sistema_comercio.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erickresend.sistema_comercio.databinding.CustomersFragmentBinding

class CustomersFragment : Fragment() {

    private lateinit var _binding: CustomersFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomersFragmentBinding.inflate(inflater)
        return _binding.root
    }
}