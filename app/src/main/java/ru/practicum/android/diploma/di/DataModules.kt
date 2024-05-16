package ru.practicum.android.diploma.di

import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.ExternalNavigator
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.Convertor
import ru.practicum.android.diploma.data.network.HHApi
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient

val dataModules = module {
//    Network
    single<NetworkClient> { RetrofitNetworkClient(androidContext(), hhApi = get()) }
    single<HHApi> {
        Retrofit.Builder().baseUrl("https://api.hh.ru/").addConverterFactory(GsonConverterFactory.create()).client(
            OkHttpClient.Builder().addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.HH_ACCESS_TOKEN}")
                    .addHeader("HH-User-Agent", "Sugar Hunter (fantasmas.dev@gmail.com)")
                val request = requestBuilder.build()
                chain.proceed(request)
            }.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        ).build().create(HHApi::class.java)
    }

    single { ExternalNavigator(context = androidContext()) }

    factory { Convertor() }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "favourite_vacancy.db")
            .build()
    }

}
