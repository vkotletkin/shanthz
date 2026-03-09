package ru.kotletkin.shantz.spell

import org.apache.commons.pool2.impl.GenericKeyedObjectPool
import org.languagetool.JLanguageTool
import org.languagetool.rules.RuleMatch
import org.springframework.stereotype.Service
import ru.kotletkin.shantz.spell.dto.SpellDTO
import ru.kotletkin.shantz.spell.dto.SpellLanguage

@Service
class SpellService(private val langToolPool: GenericKeyedObjectPool<String, JLanguageTool>) {

    fun checkSpellingOnLanguage(spellDTO: SpellDTO): List<RuleMatch> {

        val languageRepresentation = runCatching { SpellLanguage.valueOf(spellDTO.language).representation }
            .getOrElse { throw RuntimeException("Language ${spellDTO.language} does not exist", it) }

        var languageTool: JLanguageTool? = null
        return try {
            languageTool = langToolPool.borrowObject(languageRepresentation)
            languageTool.check(spellDTO.text)
        } catch (e: Exception) {
            throw RuntimeException("ERROR WITH CHECK DETECT", e) // TODO
        } finally {
            languageTool?.let { langToolPool.returnObject(languageRepresentation, it) }
        }
    }

    fun getSpellingLanguages(): List<String> {
        return SpellLanguage.LOOKUP
    }
}