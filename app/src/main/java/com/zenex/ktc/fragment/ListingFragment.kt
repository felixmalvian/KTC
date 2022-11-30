package com.zenex.ktc.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zenex.ktc.databinding.FragmentListingBinding

class ListingFragment (type: String): Fragment() {
    private var _binding: FragmentListingBinding? = null
    private val binding get() = _binding!!
    private val typeScreen = type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListingBinding.inflate(layoutInflater, container, false)

        binding.tv.text = typeScreen

        return binding.root
    }

}