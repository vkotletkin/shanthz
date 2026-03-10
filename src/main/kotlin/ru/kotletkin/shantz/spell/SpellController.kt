package ru.kotletkin.shantz.spell

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import ru.kotletkin.shantz.spell.dto.SpellDTO
import ru.kotletkin.shantz.spell.dto.SpellRequest


private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/spell")
class SpellController(private val spellService: SpellService) {

    @PostMapping
    fun checkSpell(@Valid @RequestBody spellRequest: SpellRequest): List<SpellDTO> {
        logger.info { "Запрошена валидация текста по языку: ${spellRequest.language}" }
        return spellService.checkSpellingOnLanguage(spellRequest)
    }

    @GetMapping("/languages")
    fun getSpellingLanguages(): List<String> {
        logger.info { "Запрошены поддерживаемые языки" }
        return spellService.getSpellingLanguages()
    }
}