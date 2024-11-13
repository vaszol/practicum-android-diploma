package ru.practicum.android.diploma.ui.root.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var searchBinding: FragmentSearchBinding
    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchBinding = FragmentSearchBinding.inflate(layoutInflater)
        return searchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchBinding.apply {
//            searchMagnifier.setOnClickListener(viewModel.search())    //TODO если не крестик
//            searchMagnifier.setOnClickListener { viewModel.clearText() }  //TODO если крестик
            searchEditText.apply {
                doOnTextChanged { searchText, _, _, _ ->
                    run {
                        //TODO реализовать clearIcon через searchMagnifier
                        if (searchText?.isNotEmpty() == true) {
                            //searchBinding.searchMagnifier     //TODO надеть крестик
                        } else {
                            //searchBinding.searchMagnifier     //TODO снять крестик
                        }

                        if (searchBinding.searchEditText.hasFocus() && searchText?.isEmpty() == true)
                            viewModel.showImage()
                        else
                            viewModel.searchDebounce()
                    }
                }
                doAfterTextChanged { searchText -> viewModel.doAfterTextChanged(searchText.toString()) }
                setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        viewModel.search()
                    }
                    false
                }
                setOnFocusChangeListener { _, hasFocus -> viewModel.focusChange(hasFocus) }
            }

        }
        viewModel.state.observe(viewLifecycleOwner) {
            //TODO("задать список адаптеру, менять картинки")
        }

        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                SearchScreenEvent.ClearSearch -> searchBinding.searchEditText.text.clear()
                else -> hideKeyboard()
            }
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(searchBinding.clearIcon.windowToken, 0)       //TODO снять крестик
    }
}
