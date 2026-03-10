package ru.kotletkin.shantz.spell.dto

import org.languagetool.rules.SuggestedReplacement

data class SpellDTO(
    val message: String,
    val fromPos: Int,
    val toPos: Int,
    val suggestedReplacements: List<String>
)
