package ru.kotletkin.shantz.spell

import jakarta.validation.Valid
import org.languagetool.rules.RuleMatch
import org.springframework.web.bind.annotation.*
import ru.kotletkin.shantz.spell.dto.SpellDTO

@RestController
@RequestMapping("/api/spell")
class SpellController(private val spellService: SpellService) {

    @GetMapping
    fun getSpellingLanguages(): List<String> {
        return spellService.getSpellingLanguages()
    }

    @PostMapping
    fun checkSpell(@Valid @RequestBody spellDTO: SpellDTO): List<RuleMatch> {
        return spellService.checkSpellingOnLanguage(spellDTO)
    }
}