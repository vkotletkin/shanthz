package ru.kotletkin.shantz.exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.kotletkin.shantz.exception.dto.ErrorResponse
import ru.kotletkin.shantz.exception.dto.ValidationErrorResponse
import tools.jackson.module.kotlin.KotlinInvalidNullException

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ErrorResponse {
        logger.error { ex.message }
        return ErrorResponse("Не найдено", ex.message ?: "Детали не указаны")
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(e: ConstraintViolationException): ValidationErrorResponse {

        val errorResponses = e.constraintViolations.map { violation ->
            ErrorResponse(
                title = violation.propertyPath.toString().substringAfterLast('.'),
                message = violation.message ?: "Ошибка валидации"
            )
        }

        logger.error(e) { "Ошибка валидации: ${e.message}" }

        return ValidationErrorResponse(errorResponses)
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ValidationErrorResponse {

        val errorResponses = ex.bindingResult.fieldErrors.map { error ->
            ErrorResponse(
                title = error.field,
                message = error.defaultMessage ?: "Ошибка валидации"
            )
        }

        logger.error(ex) { "Ошибка валидации на объекте: ${ex.bindingResult.objectName}" }

        return ValidationErrorResponse(errorResponses)
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException): ErrorResponse {

        val detailMessage = when (val rootCause = ex.cause) {
            is InvalidFormatException ->
                "Неверный формат поля '${rootCause.path.joinToString(".") { it.fieldName }}': ожидался тип ${rootCause.targetType.simpleName}"

            is MismatchedInputException -> "Поле '${rootCause.path.joinToString(".") { it.fieldName }}' обязательно для заполнения"

            is KotlinInvalidNullException -> {
                val fieldName = rootCause.path.joinToString(".") { it.propertyName }
                "Поле '$fieldName' является обязательным и не может быть null"
            }

            else -> "Некорректный синтаксис JSON или пропущено обязательное поле"
        }

        logger.warn { "Ошибка парсинга JSON: $detailMessage" }

        return ErrorResponse(
            title = "Ошибка чтения JSON",
            message = detailMessage
        )
    }
}