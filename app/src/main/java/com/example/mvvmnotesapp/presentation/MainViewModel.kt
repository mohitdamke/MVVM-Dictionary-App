package com.example.mvvmnotesapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmnotesapp.domain.repository.DictionaryRepository
import com.example.mvvmnotesapp.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DictionaryRepository
) : ViewModel() {

    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()

    private var searchJob: Job? = null
    init {
        _mainState.update {
            it.copy(searchWord = "Word")
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            LoadWordResult()
        }
    }

    fun onEvent(mainUiEvents: MainUiEvents) {
        when (mainUiEvents) {
            MainUiEvents.OnSearchClick -> {
                LoadWordResult()
            }

            is MainUiEvents.OnSearchWordChange -> {
                _mainState.update {
                    it.copy(
                        searchWord = mainUiEvents.newWord.lowercase()
                    )
                }
            }
        }
    }

    private fun LoadWordResult() {
        viewModelScope.launch {
            repository.getWordResult(
                _mainState.value.searchWord
            ).collect { result ->
                when (result) {
                    is Result.Error -> Unit
                    is Result.Loading -> {
                        _mainState.update {
                            it.copy(isLoading = result.isLoading)
                        }

                    }

                    is Result.Success -> {
                        result.data?.let { wordItem ->
                            _mainState.update {
                                it.copy(wordItem = wordItem)

                            }
                        }

                    }
                }


            }
        }
    }
}