package ru.practicum.android.diploma.ui.root

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
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
import java.util.Locale

class RootActivity : AppCompatActivity() {
    private val vacancyInteractor: VacancyInteractor by inject()
    private val tag: String = "RootActivity"

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
    }

    private fun networkRequestExample(accessToken: String) {
        Log.d(tag, String.format(Locale.US, "accessToken: %s", accessToken))
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vacancyInteractor.searchVacancies("", "RUR", "1")
                    .collect { pair ->
                        if (pair.second != null) {
                            Log.d(tag, String.format(Locale.US, "Ошибка: %s", pair.second))
                        } else if (pair.first.isNullOrEmpty()) {
                            Log.d(tag, "Ответ пустой")
                        } else {
                            messageOk(pair.first!!)
                        }
                    }
            }
        }
    }

    private fun messageOk(ids: List<String>) {
        Log.d(tag, String.format(Locale.US, "Ответ c размером колекции %d", ids.size))
        ids.forEach {
            Log.d(tag, String.format(Locale.US, "id: %s", it))
        }
    }
}
