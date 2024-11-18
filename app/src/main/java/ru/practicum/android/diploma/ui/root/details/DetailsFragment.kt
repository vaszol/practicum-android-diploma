package ru.practicum.android.diploma.ui.root.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private var vacancyId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vacancyId = requireArguments().getString(ARG_VACANCY)

        binding.backImg.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        const val ARG_VACANCY = "vacancyId"
        fun createArgs(vacancyId: String): Bundle = bundleOf(ARG_VACANCY to vacancyId)
    }
}
