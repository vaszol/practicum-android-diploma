package ru.practicum.android.diploma.di

import androidx.room.Room
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig.HH_ACCESS_TOKEN
import ru.practicum.android.diploma.data.NetworkClient
import ru.practicum.android.diploma.data.db.AppDataBase
import ru.practicum.android.diploma.data.network.HHApiClient
import ru.practicum.android.diploma.data.network.HhApi

private const val HHBaseUrl = "https://api.hh.ru"
val dataModule = module {

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(HHBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
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

    factory<OkHttpClient> {
        Builder()
            .addInterceptor {
                val original = it.request()
                val request = original.newBuilder()
                    .header("Authorization", "Bearer $HH_ACCESS_TOKEN")
                    .header("HH-User-Agent", "YP Diploma Project (vaszol@mail.ru)")
                    .method(original.method(), original.body())
                    .build()
                it.proceed(request)
            }.build()
    }
}
