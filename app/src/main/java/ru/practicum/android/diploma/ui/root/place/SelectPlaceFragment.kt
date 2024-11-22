package ru.practicum.android.diploma.ui.root.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSelectPlaceBinding

class SelectPlaceFragment : Fragment() {

    private var _binding: FragmentSelectPlaceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            selectCountryButton.setOnClickListener {
                findNavController().navigate(R.id.action_selectPlaceFragment_to_selectCountryFragment)
            }
            selectRegionButton.setOnClickListener {
                findNavController().navigate(R.id.action_selectPlaceFragment_to_selectRegionFragment)
            }
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}
