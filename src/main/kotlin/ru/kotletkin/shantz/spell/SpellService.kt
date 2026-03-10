package ru.kotletkin.shantz.spell

import org.apache.commons.pool2.impl.GenericKeyedObjectPool
import org.languagetool.JLanguageTool
import org.languagetool.rules.RuleMatch
import org.springframework.stereotype.Service
import ru.kotletkin.shantz.exception.NotFoundException
import ru.kotletkin.shantz.exception.SpellCheckingException
import ru.kotletkin.shantz.spell.dto.SpellDTO
import ru.kotletkin.shantz.spell.dto.SpellLanguage
import ru.kotletkin.shantz.spell.dto.SpellRequest

@Service
class SpellService(private val langToolPool: GenericKeyedObjectPool<String, JLanguageTool>) {

    fun checkSpellingOnLanguage(spellRequest: SpellRequest): List<SpellDTO> {

        val languageRepresentation = runCatching { SpellLanguage.valueOf(spellRequest.language).representation }
            .getOrElse { throw NotFoundException("Язык с именем: ${spellRequest.language} - не найден") }

        var languageTool: JLanguageTool? = null
        val matches = try {
            languageTool = langToolPool.borrowObject(languageRepresentation)
            languageTool.check(spellRequest.text)
        } catch (_: Exception) {
            throw SpellCheckingException("Ошибка при проверке текста на наличие ошибок")
        } finally {
            languageTool?.let { langToolPool.returnObject(languageRepresentation, it) }
        }

        val corrections = matches.map { SpellDTO(it.message, it.fromPos, it.toPos, it.suggestedReplacements) }
        return corrections
    }

    fun getSpellingLanguages(): List<String> {
        return SpellLanguage.LOOKUP
    }
}