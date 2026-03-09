package ru.kotletkin.shantz.config.langtool

import org.apache.commons.pool2.KeyedPooledObjectFactory
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.impl.DefaultPooledObject
import org.languagetool.JLanguageTool
import org.languagetool.Languages

class LangToolPooledObjectFactory : KeyedPooledObjectFactory<String, JLanguageTool> {

    override fun makeObject(languageKey: String): PooledObject<JLanguageTool> {
        val language = Languages.getLanguageForShortCode(languageKey)
        val languageTool = JLanguageTool(language)
        return DefaultPooledObject(languageTool)
    }

    override fun validateObject(languageKey: String, pooledObject: PooledObject<JLanguageTool>): Boolean {
        return runCatching { pooledObject.`object`.allActiveRules }.isSuccess
    }

    override fun activateObject(p0: String?, p1: PooledObject<JLanguageTool>) {}

    override fun destroyObject(p0: String?, p1: PooledObject<JLanguageTool>) {}

    override fun passivateObject(p0: String?, p1: PooledObject<JLanguageTool?>?) {}
}