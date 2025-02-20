package com.example.cafelabservice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.io.FileNotFoundException

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(FileNotFoundException::class)
    fun handleFileNotFoundException(ex: FileNotFoundException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity("File not found: ${ex.message}", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity("Resource not found: ${ex.message}", HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception, request: WebRequest): ResponseEntity<String> {
        return ResponseEntity("An error occurred: ${ex.message}", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}