package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.AreaDto
import ru.practicum.android.diploma.data.dto.IndustryDto
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

    override suspend fun getVacancies(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                when {
                    !isConnected() -> Response().apply { resultCode = -1 }

                    dto is VacanciesRequest -> hhApi.getVacancies(dto.text, dto.currency, dto.size, dto.page).apply {
                        resultCode = HttpsURLConnection.HTTP_OK
                    }

                    else -> Response().apply { resultCode = HttpsURLConnection.HTTP_BAD_REQUEST }
                }
            } catch (exception: HttpException) {
                Log.d("Exception caught in HHApiClient", exception.message())
                Response().apply { resultCode = HttpsURLConnection.HTTP_BAD_REQUEST }
            }
        }
    }

    override suspend fun getVacancy(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                when {
                    !isConnected() -> Response().apply { resultCode = -1 }

                    dto is VacancyRequest -> hhApi.getVacancy(dto.id, dto.locale, dto.host).body().let {
                        VacancyResponse(it).apply { resultCode = HttpsURLConnection.HTTP_OK }
                    }

                    else -> Response().apply { resultCode = HttpsURLConnection.HTTP_BAD_REQUEST }
                }
            } catch (exception: HttpException) {
                Log.d("Exception caught in HHApiClient: $exception", exception.message())
                Response().apply { resultCode = HttpsURLConnection.HTTP_BAD_REQUEST }
            }
        }
    }

    override suspend fun getIndustries(dto: Any): List<IndustryDto> {
        return withContext(Dispatchers.IO) {
            try {
                when {
                    !isConnected() -> emptyList()

                    dto is LocaleRequest -> hhApi.getIndustries(dto.locale, dto.host)

                    else -> emptyList()
                }
            } catch (exception: HttpException) {
                Log.d("Exception caught in HHApiClient: $exception", exception.message())
                emptyList()
            }
        }
    }

    override suspend fun getAreas(dto: Any): List<AreaDto> {
        return withContext(Dispatchers.IO) {
            try {
                when {
                    !isConnected() -> emptyList()

                    dto is LocaleRequest -> hhApi.getAreas(dto.locale, dto.host)

                    else -> emptyList()
                }
            } catch (exception: HttpException) {
                Log.d("Exception caught in HHApiClient: $exception", exception.message())
                emptyList()
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
