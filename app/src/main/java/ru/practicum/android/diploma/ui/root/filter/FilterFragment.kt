package ru.practicum.android.diploma.ui.root.filter

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.presentation.filter.FilterViewModel
import ru.practicum.android.diploma.ui.root.RootActivity

class FilterFragment : Fragment() {
    private val viewModel: FilterViewModel by viewModel()
    private val binding by lazy { FragmentFilterBinding.inflate(layoutInflater) }

    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? RootActivity)?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.visibility = View.GONE

        setupListeners()
        setupTextWatchers()
    }

    private fun setupListeners() {
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.apply.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.reset.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.checkBox.setOnClickListener {
            isClickAllowed = !isClickAllowed
            if (isClickAllowed) {
                binding.checkBox.setImageResource(R.drawable.ic_check_box_mark)
            } else {
                binding.checkBox.setImageResource(R.drawable.ic_check_box_unmark)
            }
        }
        binding.deleteSalary.setOnClickListener {
            binding.salary.text.clear()
        }
    }

    private fun setupTextWatchers() {
        binding.salary.addTextChangedListener(
            afterTextChanged = { s: Editable? ->
                val context = binding.expectedSalary.context
                val colorOnSecondary = context.getThemeColor(com.google.android.material.R.attr.colorOnSecondary)
                val colorAccent = context.getThemeColor(org.koin.android.R.attr.colorAccent)

                if (!s.isNullOrEmpty()) {
                    binding.expectedSalary.setTextColor(colorAccent)
                    binding.deleteSalary.isVisible = true
                } else {
                    binding.expectedSalary.setTextColor(colorOnSecondary)
                    binding.deleteSalary.isVisible = false
                }
            }
        )
    }

    private fun Context.getThemeColor(attr: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attr, typedValue, true)
        return typedValue.data
    }
}
