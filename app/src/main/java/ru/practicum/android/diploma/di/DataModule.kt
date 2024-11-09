package ru.practicum.android.diploma.di

import com.google.gson.Gson
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.network.HHApiClient
import ru.practicum.android.diploma.data.network.HhApi

private const val hhBaseUrl = "https://api.hh.ru"
val dataModule = module {

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(hhBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<HhApi> {
        get<Retrofit>().create(HhApi::class.java)
    }

    single<NetworkClient> {
        HHApiClient(get(), get())
    }

    factory<Gson> { Gson() }
}
