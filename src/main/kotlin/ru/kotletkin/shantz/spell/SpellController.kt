package ru.kotletkin.shantz.spell

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.Valid
import org.languagetool.rules.RuleMatch
import org.springframework.web.bind.annotation.*
import ru.kotletkin.shantz.spell.dto.SpellDTO


private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/spell")
class SpellController(private val spellService: SpellService) {

    @PostMapping
    fun checkSpell(@Valid @RequestBody spellDTO: SpellDTO): List<RuleMatch> {
        logger.info { "Запрошена валидация текста по языку: ${spellDTO.language}" }
        return spellService.checkSpellingOnLanguage(spellDTO)
    }

    @GetMapping("/languages")
    fun getSpellingLanguages(): List<String> {
        logger.info { "Запрошены поддерживаемые языки" }
        return spellService.getSpellingLanguages()
    }
}