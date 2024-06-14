package com.example.mvvmnotesapp.presentation

import com.example.mvvmnotesapp.domain.model.WordItem

data class MainState(
    val isLoading: Boolean = false,
    val searchWord : String = "",
    val wordItem : WordItem? = null,
)