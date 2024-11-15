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
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.domain.api.VacancyInteractor
import ru.practicum.android.diploma.domain.models.Host
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

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vacancyInteractor.searchVacancy("108765218", "RU", Host.HH_RU)
                    .collect { pair ->
                        if (pair.second != null) {
                            Log.d(tag, String.format(Locale.US, "Ошибка: %s", pair.second))
                        } else if (pair.first == null) {
                            Log.d(tag, "Ответ пустой")
                        } else {
                            val vacancy = pair.first
                            Log.d(tag, String.format(Locale.US, "Vacancy.id %s", vacancy?.id))
                            Log.d(tag, String.format(Locale.US, "Vacancy.name %s", vacancy?.name))
                            Log.d(tag, String.format(Locale.US, "Vacancy.employerName %s", vacancy?.employerName))
                            Log.d(tag, String.format(Locale.US, "Vacancy.experience %s", vacancy?.experience))
                            Log.d(tag, String.format(Locale.US, "Vacancy.employment %s", vacancy?.employment))
                            Log.d(tag, String.format(Locale.US, "Vacancy.schedule %s", vacancy?.schedule))
                            Log.d(tag, String.format(Locale.US, "Vacancy.salaryFrom %s", vacancy?.salaryFrom))
                            Log.d(tag, String.format(Locale.US, "Vacancy.salaryTo %s", vacancy?.salaryTo))
                            Log.d(tag, String.format(Locale.US, "Vacancy.keySkills %s", vacancy?.keySkills?.size ?: 0))
                            vacancy?.keySkills!!.map {
                                Log.d(tag, String.format(Locale.US, "Vacancy.keySkill %s", it))
                            }
                            Log.d(tag, String.format(Locale.US, "Vacancy.street %s", vacancy?.street))
                            Log.d(tag, String.format(Locale.US, "Vacancy.building %s", vacancy?.building))
                            Log.d(tag, String.format(Locale.US, "Vacancy.url %s", vacancy?.url))
                            Log.d(tag, String.format(Locale.US, "Vacancy.contactsName %s", vacancy?.contactsName))
                            Log.d(tag, String.format(Locale.US, "Vacancy.contactsEmail %s", vacancy?.contactsEmail))
                            Log.d(tag, String.format(Locale.US, "Vacancy.description %s", vacancy?.description))
                            Log.d(tag, String.format(Locale.US, "Vacancy.currency %s", vacancy?.currency))
                            Log.d(
                                tag,
                                String.format(Locale.US, "Vacancy.employerLogoUrl90 %s", vacancy?.employerLogoUrl90)
                            )
                        }
                    }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vacancyInteractor.searchLocales("RU", Host.HH_RU)
                    .collect {
                        Log.d(tag, String.format(Locale.US, "Ответ c размером колекции %d", it.size))
                        it.map { locale ->
                            Log.d(tag, String.format(Locale.US, "locale.id %s", locale.id))
                            Log.d(tag, String.format(Locale.US, "locale.name %s", locale.name))
                            Log.d(tag, String.format(Locale.US, "locale.current %s", locale.current))
                        }
                    }
            }
        }
    }
}
