package ru.practicum.android.diploma.ui.root.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.practicum.android.diploma.databinding.FragmentDetailsBinding
import ru.practicum.android.diploma.presentation.details.DetailsViewModel
import ru.practicum.android.diploma.ui.root.details.models.StateVacancyDetails

class DetailsFragment : Fragment() {

    open val viewModel: DetailsViewModel by viewModel {
        parametersOf(vacancyId)
    }
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private var vacancyId: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vacancyId = requireArguments().getString(ARG_VACANCY)

        binding.backImg.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.detailsScreenState.observe(viewLifecycleOwner, ::render)
    }

    private fun render(state: StateVacancyDetails) {
        when (state) {
            is StateVacancyDetails.Content -> {
                Details(requireContext(), binding).getContent(state.vacancy)
            }

            is StateVacancyDetails.Loading -> {
                showLoading()
            }

            else -> showError()

        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.isVisible = true
            vacancyError.isVisible = false
            scrollView.isVisible = false
        }
    }

    private fun showError() {
        with(binding) {
            progressBar.isVisible = false
            vacancyError.isVisible = true
            scrollView.isVisible = false

        }
    }

    companion object {
        const val ARG_VACANCY = "vacancyId"
        fun createArgs(vacancyId: String): Bundle = bundleOf(ARG_VACANCY to vacancyId)
    }
}
