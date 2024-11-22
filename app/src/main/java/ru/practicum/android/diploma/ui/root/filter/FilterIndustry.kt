package ru.practicum.android.diploma.ui.root.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.practicum.android.diploma.databinding.FragmentFilterIndustryBinding

class FilterIndustry : Fragment() {
    private val binding by lazy { FragmentFilterIndustryBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.apply {
            backImg.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
            buttonIndustry.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        }
    }
}
