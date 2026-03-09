package ru.kotletkin.shantz

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShantzApplication

fun main(args: Array<String>) {
	runApplication<ShantzApplication>(*args)
}
