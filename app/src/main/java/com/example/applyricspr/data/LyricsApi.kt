package com.example.applyricspr.data

import retrofit2.http.GET
import retrofit2.http.Query

import retrofit2.Response

interface LyricsApi {
    @GET("api/search")
    suspend fun searchLyrics(
        @Query("track_name") trackName: String,
        @Query("artist_name") artistName: String,
        @Query("album_name") albumName: String?,
        @Query("duration") duration: Int?
    ): Response<List<LyricsResponse>>
}