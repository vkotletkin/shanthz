package ru.kotletkin.shantz.exception.dto

import java.time.Instant

data class ErrorResponse(val title: String, val message: String, val timestamp: Instant = Instant.now())