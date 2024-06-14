package com.example.mvvmnotesapp.data.mapper

import com.example.mvvmnotesapp.data.dto.DefinitionDto
import com.example.mvvmnotesapp.data.dto.MeaningDto
import com.example.mvvmnotesapp.data.dto.WordItemDto
import com.example.mvvmnotesapp.domain.model.Definition
import com.example.mvvmnotesapp.domain.model.Meaning
import com.example.mvvmnotesapp.domain.model.WordItem


fun WordItemDto.toWordItem() = WordItem (
    word = word ?: "",
    meanings = meanings?.map {
        it.toMeaning()
    } ?: emptyList(),
    phonetic = phonetic ?: ""
)

fun MeaningDto.toMeaning() = Meaning(
    definitions = definitionDtoToDefinition(definitions?.get(0)),
    partOfSpeech = partOfSpeech ?: ""
)


fun definitionDtoToDefinition(
    definitionDto: DefinitionDto?
) = Definition(
    definition = definitionDto?.definition ?: "",
    example = definitionDto?.example ?: ""
)


