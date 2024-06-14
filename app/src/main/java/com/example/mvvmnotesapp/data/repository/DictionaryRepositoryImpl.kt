package com.example.mvvmnotesapp.data.repository

import android.app.Application
import com.example.mvvmnotesapp.data.api.DictionaryApi
import com.example.mvvmnotesapp.data.mapper.toWordItem
import com.example.mvvmnotesapp.domain.model.WordItem
import com.example.mvvmnotesapp.domain.repository.DictionaryRepository
import com.example.mvvmnotesapp.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val api: DictionaryApi,
    private val application: Application
) : DictionaryRepository {
    override suspend fun getWordResult(word: String):
            Flow<Result<WordItem>> {
        return flow {
            emit(Result.Loading(true))

            val remoteWordResultDto = try {
                api.getWordResult(word)
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error("This tis the error"))
                emit(Result.Loading(false))
                return@flow
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Result.Error("It was error in IO"))
                emit(Result.Loading(false))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error("application.getString(R.string.can_t_get_result"))
                emit(Result.Loading(false))
                return@flow
            }

            remoteWordResultDto?.let { wordResultDto ->
                wordResultDto[0]?.let{ wordItemDto ->
                    emit(Result.Success(wordItemDto.toWordItem()))
                    emit(Result.Loading(false))
                    return@flow
                }
            }
        }
    }
}













