package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.db.AppDataBase

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "app_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
