package ru.practicum.android.diploma.ui.root.details

import android.content.Context
import android.text.Html
import android.widget.ImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentDetailsBinding
import ru.practicum.android.diploma.domain.models.VacancyDetail

open class Details(private val context: Context, private val binding: FragmentDetailsBinding) {

    fun getContent(details: VacancyDetail) {
        binding.apply {
            progressBar.isVisible = false
            scrollView.isVisible = true
            vacancyNameTitle.text = details.name
            salaryTxt.text = details.salaryFrom.toString()
            employerName.text = details.employerName
            areaName.text = details.area.toString()
            getLogo(vacancyLogo, details.employerLogoUrl90)
            requiredXp.text = details.experience
            busynessTxt.text = details.employment
            descriptionTxt.text = Html.fromHtml(details.description, Html.FROM_HTML_MODE_COMPACT)
            getKeySkills(details)
        }
    }

    private fun getLogo(imageView: ImageView, url: String?) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.ic_logotype_vacancy)
            .transform(RoundedCorners(12))
            .into(imageView)
    }
    private fun getKeySkills(details: VacancyDetail) {
        binding.apply {
            if (details.keySkills.isNotEmpty()) {
                keySkillsTxt.text = details.keySkills.toString()
            } else {
                keySkillsTxt.isVisible = false
            }
        }
    }
}
