package ru.kotletkin.shantz.spell

import org.apache.commons.pool2.impl.GenericKeyedObjectPool
import org.languagetool.JLanguageTool
import org.languagetool.rules.RuleMatch
import org.springframework.stereotype.Service
import ru.kotletkin.shantz.exception.NotFoundException
import ru.kotletkin.shantz.exception.SpellCheckingException
import ru.kotletkin.shantz.spell.dto.SpellDTO
import ru.kotletkin.shantz.spell.dto.SpellLanguage

@Service
class SpellService(private val langToolPool: GenericKeyedObjectPool<String, JLanguageTool>) {

    fun checkSpellingOnLanguage(spellDTO: SpellDTO): List<RuleMatch> {

        val languageRepresentation = runCatching { SpellLanguage.valueOf(spellDTO.language).representation }
            .getOrElse { throw NotFoundException("Язык с именем: ${spellDTO.language} - не найден") }

        var languageTool: JLanguageTool? = null
        val matches = try {
            languageTool = langToolPool.borrowObject(languageRepresentation)
            languageTool.check(spellDTO.text)
        } catch (_: Exception) {
            throw SpellCheckingException("Ошибка при проверке текста на наличие ошибок")
        } finally {
            languageTool?.let { langToolPool.returnObject(languageRepresentation, it) }
        }

        return emptyList()
    }

    fun getSpellingLanguages(): List<String> {
        return SpellLanguage.LOOKUP
    }
}