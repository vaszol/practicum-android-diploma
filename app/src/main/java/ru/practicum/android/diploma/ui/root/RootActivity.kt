package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.domain.api.VacancyInteractor

class RootActivity : AppCompatActivity() {
    private var _binding: ActivityRootBinding? = null
    private val binding get() = _binding!!
    private val vacancyInteractor: VacancyInteractor by inject()
    private val TAG: String = "RootActivity";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun networkRequestExample(accessToken: String) {
        Log.d(TAG, String.format("accessToken: %s", accessToken))
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vacancyInteractor.searchVacancies("")
                    .collect { pair ->
                        if (pair.second != null) Log.d(TAG, String.format("Ошибка: %s", pair.second));
                        else if (pair.first.isNullOrEmpty()) Log.d(TAG, "Ответ пустой")
                        else messageOk(pair.first!!)
                    }
            }
        }
    }

    private fun messageOk(ids: List<String>) {
        Log.d(TAG, String.format("Ответ c размером колекции %s", ids.size))
        ids.forEach {
            Log.d(TAG, String.format("id: %s", it))
        }
    }
}
