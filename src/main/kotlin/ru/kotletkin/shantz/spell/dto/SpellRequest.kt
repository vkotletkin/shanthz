package ru.kotletkin.shantz.spell.dto

import jakarta.validation.constraints.NotBlank

data class SpellRequest(
    @field:NotBlank("Текст не может быть пустым")
    val text: String,
    @field:NotBlank("Язык не может быть пустым")
    val language: String
)