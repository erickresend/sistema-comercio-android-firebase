package com.erickresend.sistema_comercio.ui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.erickresend.sistema_comercio.R
import com.erickresend.sistema_comercio.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    private lateinit var _binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.btnProducts.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_productsFragment)
        }

        _binding.btnUsers.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_usersFragment)
        }

        _binding.btnCustomers.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_customersFragment)
        }
    }
}