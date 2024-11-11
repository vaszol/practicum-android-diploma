package ru.practicum.android.diploma.di

import com.google.gson.Gson
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.network.HHApiClient
import ru.practicum.android.diploma.data.network.HhApi
import ru.practicum.android.diploma.data.db.AppDataBase

private const val HHBaseUrl = "https://api.hh.ru"
val dataModule = module {

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(HHBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<HhApi> {
        get<Retrofit>().create(HhApi::class.java)
    }

    single<NetworkClient> {
        HHApiClient(get(), get())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "app_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    factory<Gson> { Gson() }
}
