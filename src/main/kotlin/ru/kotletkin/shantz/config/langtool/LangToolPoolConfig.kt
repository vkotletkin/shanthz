package ru.kotletkin.shantz.config.langtool

import org.apache.commons.pool2.impl.GenericKeyedObjectPool
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig
import org.languagetool.JLanguageTool
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration;

@Configuration
class LangToolPoolConfig {

    @Bean
    fun languageToolFactory() = LangToolPooledObjectFactory()

    @Bean
    fun languageToolPool(factory: LangToolPooledObjectFactory): GenericKeyedObjectPool<String, JLanguageTool> {

        val config = GenericKeyedObjectPoolConfig<JLanguageTool>().apply {
            maxTotalPerKey = 5
            maxTotal = 20
            blockWhenExhausted = true
            setMaxWait(Duration.ofSeconds(15))
            testOnBorrow = false
            testOnReturn = false
            minEvictableIdleDuration = Duration.ofMinutes(10)
            timeBetweenEvictionRuns = Duration.ofSeconds(30)
            jmxEnabled = false
        }

        return GenericKeyedObjectPool(factory, config)
    }
}