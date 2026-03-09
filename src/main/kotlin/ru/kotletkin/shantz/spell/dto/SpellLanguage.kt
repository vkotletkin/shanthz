package ru.kotletkin.shantz.spell.dto

enum class SpellLanguage(val representation: String) {
    ENG("en");

    companion object {
        val LOOKUP: List<String> = entries.map { it.name }
    }
}