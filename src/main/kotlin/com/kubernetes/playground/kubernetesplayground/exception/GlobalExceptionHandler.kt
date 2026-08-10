package com.kubernetes.playground.kubernetesplayground.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        logger.error("=================== [BACKEND VALIDATION FAILED] ===================")
        logger.error("Request object validation failed with {} error(s):", ex.bindingResult.errorCount)

        val fieldErrors = mutableMapOf<String, String>()
        ex.bindingResult.fieldErrors.forEach { error ->
            fieldErrors[error.field] = error.defaultMessage ?: "Invalid value"
            logger.error(" -> Field '{}': Rejected Value = [{}], Reason = [{}]",
                error.field, error.rejectedValue, error.defaultMessage)
        }
        logger.error("===================================================================")

        val errorResponse = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to "Bad Request - Validation Error",
            "message" to "Backend validation failed for one or more fields",
            "fieldErrors" to fieldErrors
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<Map<String, Any>> {
        logger.error("=================== [MALFORMED JSON / INVALID ENUM] ===================")
        logger.error("Failed to parse request JSON payload: {}", ex.localizedMessage)
        logger.error("=======================================================================")

        val errorResponse = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to "Bad Request - Invalid Payload",
            "message" to "Malformed JSON payload or invalid Enum value (Gender must be 'Male' or 'Female')",
            "details" to (ex.mostSpecificCause.message ?: ex.message ?: "Invalid request body format")
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(ex: NoSuchElementException): ResponseEntity<Map<String, Any>> {
        logger.warn("Resource Not Found: {}", ex.message)
        val errorResponse = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "status" to HttpStatus.NOT_FOUND.value(),
            "error" to "Not Found",
            "message" to (ex.message ?: "Resource not found")
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<Map<String, Any>> {
        logger.error("Unexpected Internal Server Error: ", ex)
        val errorResponse = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "status" to HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "error" to "Internal Server Error",
            "message" to (ex.message ?: "An unexpected error occurred")
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}
