package com.example.mvvmnotesapp.data.api

import com.example.mvvmnotesapp.data.dto.WordItemDto
import com.example.mvvmnotesapp.data.dto.WordResultDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    @GET("{word}")
    suspend fun getWordResult(
        @Path("word") word: String
    ): WordResultDto?

companion object {
    const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"

}
}