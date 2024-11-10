package ru.practicum.android.diploma.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import javax.net.ssl.HttpsURLConnection

class HHApiClient(private val hhApi: HhApi, private val context: Context) : NetworkClient {

    override suspend fun vacancies(dto: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                if (!isConnected()) {
                    Response().apply { resultCode = -1 }
                }
                if (dto is VacanciesRequest) {
                    hhApi.getVacancies(dto.text, dto.currency, dto.size, dto.page).apply {
                        resultCode = HttpsURLConnection.HTTP_OK
                    }
                } else {
                    Response().apply { resultCode = HttpsURLConnection.HTTP_BAD_REQUEST }
                }
            } catch (exception: Exception) {
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
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}
