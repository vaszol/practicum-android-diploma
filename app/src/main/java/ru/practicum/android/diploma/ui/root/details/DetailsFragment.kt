package ru.practicum.android.diploma.ui.root.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentDetailsBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.presentation.details.DetailsScreenState
import ru.practicum.android.diploma.presentation.details.DetailsViewModel
import ru.practicum.android.diploma.ui.root.RootActivity
import ru.practicum.android.diploma.util.extentions.getFormattedSalary

class DetailsFragment : Fragment() {
    private val viewModel: DetailsViewModel by viewModel()
    private val binding by lazy { FragmentDetailsBinding.inflate(layoutInflater) }
    private var currentVacancy: VacancyDetail? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as RootActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view).visibility =
            View.GONE
        val vacancyId = arguments?.getString(VACANCY_ID) ?: return
        viewModel.loadVacancyDetails(vacancyId)

        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DetailsScreenState.Loading -> showLoading()
                is DetailsScreenState.Error -> showError(state.isServerError)
                is DetailsScreenState.Content -> showVacancyDetails(state.vacancy, state.isFavorite)
            }

            with(binding) {
                favoriteOff.setOnClickListener {
                    viewModel.addToFavorite(currentVacancy!!)
                    binding.favoriteOff.visibility = View.GONE
                    binding.favoriteOn.visibility = View.VISIBLE
                }
                favoriteOn.setOnClickListener {
                    viewModel.removeFromFavorite(currentVacancy!!)
                    binding.favoriteOff.visibility = View.VISIBLE
                    binding.favoriteOn.visibility = View.GONE
                }
                shareImg.setOnClickListener {
                    shareVacancyDetails()
                }
                binding.backImg.setOnClickListener { findNavController().popBackStack() }
            }
        }
    }

    private fun showVacancyDetails(vacancy: VacancyDetail, isFavorite: Boolean) {
        currentVacancy = vacancy
        with(binding) {
            progressBar.visibility = View.GONE
            vacancyNameTitle.text = vacancy.name
            salaryTxt.text = getFormattedSalary(vacancy.salaryFrom, vacancy.salaryTo, vacancy.currency)
            employerName.text = vacancy.employerName
            requiredXp.text = vacancy.experience
            val address = if (vacancy.street != Vacancy.VACANCY_DEFAULT_STRING_VALUE) {
                if (vacancy.building != Vacancy.VACANCY_DEFAULT_STRING_VALUE) {
                    "${vacancy.area.name}, ${vacancy.street}, ${vacancy.building}"
                } else {
                    "${vacancy.area.name}, ${vacancy.street}"
                }
            } else {
                vacancy.area.name
            }
            areaName.text = address
            busynessTxt.text = vacancy.employment
            keySkillsTxt.text = vacancy.keySkills.joinToString(separator = "") {
                "&#8226; $it<br>"
            }.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
            if (keySkillsTxt.text.isEmpty()) {
                keySkillsTitle.visibility = View.GONE
            } else {
                keySkillsTitle.visibility = View.VISIBLE
            }
            descriptionTxt.text = HtmlCompat.fromHtml(vacancy.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
            Glide.with(vacancyLogo.context)
                .load(vacancy.employerLogoUrl90)
                .placeholder(R.drawable.employer_logo_placeholder)
                .into(vacancyLogo)
        }
        renderIfFavorite(isFavorite)
    }

    private fun renderIfFavorite(isFavorite: Boolean) {
        if (isFavorite) {
            binding.favoriteOff.visibility = View.GONE
            binding.favoriteOn.visibility = View.VISIBLE
        } else {
            binding.favoriteOff.visibility = View.VISIBLE
            binding.favoriteOn.visibility = View.GONE
        }
    }

    private fun showLoading() {
        with(binding) {
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showError(isServerError: Boolean) {
        with(binding) {
            progressBar.visibility = View.GONE
            if (isServerError) {
                vacancyErrorImage.setImageResource(R.drawable.placeholder_vacancy_error)
                vacancyErrorTxt.setText(R.string.vacancy_error_server)
            } else {
                vacancyErrorImage.setImageResource(R.drawable.placeholder_vacancy_empty_or_not)
                vacancyErrorTxt.setText(R.string.vacancy_error)
            }
            vacancyError.visibility = View.VISIBLE
            vacancyErrorTxt.visibility = View.VISIBLE
        }
    }

    private fun shareVacancyDetails() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, currentVacancy!!.url)
        }
        startActivity(Intent.createChooser(shareIntent, "Share Vacancy"))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as RootActivity)
            .findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
            .visibility = View.VISIBLE
    }

    companion object {
        const val VACANCY_ID = "VACANCY_ID"
    }
}
