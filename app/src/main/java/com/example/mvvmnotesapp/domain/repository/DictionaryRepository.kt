package com.example.mvvmnotesapp.domain.repository

import com.example.mvvmnotesapp.domain.model.WordItem
import com.example.mvvmnotesapp.util.Result
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    suspend fun getWordResult(word: String):
            Flow<Result<WordItem>>
}