package com.example.applyricspr.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singletonowy obiekt, który tworzy instancję Retrofit do komunikacji z API
object RetrofitInstance {

    val api: LyricsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://lrclib.net/")  // Adres bazowy API (punkt początkowy zapytań HTTP)
            .addConverterFactory(GsonConverterFactory.create())  // Użycie konwertera Gson do automatycznego przekształcania JSON <-> obiekty Kotlin
            .build()  // Zbudowanie obiektu Retrofit
            .create(LyricsApi::class.java)  // Utworzenie implementacji interfejsu LyricsApi (komunikacja z endpointami)
    }
}
