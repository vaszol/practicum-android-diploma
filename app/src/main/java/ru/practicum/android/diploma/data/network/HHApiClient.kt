package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.DictionariesResponse
import ru.practicum.android.diploma.data.dto.LocaleDto
import ru.practicum.android.diploma.data.dto.LocaleRequest
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacancyRequest
import ru.practicum.android.diploma.data.dto.VacancyResponse
import javax.net.ssl.HttpsURLConnection

class HHApiClient(
    private val hhApi: HhApi,
    private val context: Context
) : NetworkClient {

    override suspend fun vacancies(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                if (!isConnected()) {
                    Response().apply { resultCode = -1 }
                } else if (dto is VacanciesRequest) {
                    hhApi.getVacancies(dto.text, dto.currency, dto.size, dto.page).apply {
                        resultCode = HttpsURLConnection.HTTP_OK
                    }
                } else {
                    Response().apply { resultCode = HttpsURLConnection.HTTP_BAD_REQUEST }
                }
            } catch (exception: HttpException) {
                Log.d("Exception caught in HHApiClient: $exception", exception.message())
                Response().apply { resultCode = HttpsURLConnection.HTTP_BAD_REQUEST }
            }
        }
    }

    override suspend fun vacancy(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                if (!isConnected()) {
                    Response().apply { resultCode = -1 }
                } else if (dto is VacancyRequest) {
                    hhApi.getVacancy(dto.id, dto.locale, dto.host).body().let {
                        VacancyResponse(it!!).apply { resultCode = HttpsURLConnection.HTTP_OK }
                    }
                } else {
                    Response().apply { resultCode = HttpsURLConnection.HTTP_BAD_REQUEST }
                }
            } catch (exception: HttpException) {
                Log.d("Exception caught in HHApiClient: $exception", exception.message())
                Response().apply { resultCode = HttpsURLConnection.HTTP_BAD_REQUEST }
            }
        }
    }

    override suspend fun locales(dto: Any): List<LocaleDto> {
        return withContext(Dispatchers.IO) {
            try {
                if (!isConnected()) {
                    emptyList()
                } else if (dto is LocaleRequest) {
                    hhApi.getLocales(dto.locale, dto.host)
                } else {
                    emptyList()
                }
            } catch (exception: HttpException) {
                Log.d("Exception caught in HHApiClient: $exception", exception.message())
                emptyList()
            }
        }
    }

    override suspend fun dictionaries(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                if (!isConnected()) {
                    Response().apply { resultCode = -1 }
                } else if (dto is VacancyRequest) {
                    hhApi.getDictionaries(dto.locale, dto.host).body().let {
                        DictionariesResponse(it!!).apply { resultCode = HttpsURLConnection.HTTP_OK }
                    }
                } else {
                    Response().apply { resultCode = HttpsURLConnection.HTTP_BAD_REQUEST }
                }
            } catch (exception: HttpException) {
                Log.d("Exception caught in HHApiClient: $exception", exception.message())
                Response().apply { resultCode = HttpsURLConnection.HTTP_BAD_REQUEST }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities != null && (
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            )
    }
}
