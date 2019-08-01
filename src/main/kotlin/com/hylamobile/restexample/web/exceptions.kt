package com.hylamobile.restexample.web

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.data.rest.core.RepositoryConstraintViolationException as RepoValidationEx
import org.springframework.http.ResponseEntity
import org.springframework.validation.ObjectError

typealias ErrorResponse = ResponseEntity<List<ObjectError>>

@ControllerAdvice
class RestValidationExceptionHandler {

    @ExceptionHandler(RepoValidationEx::class)
    fun handleRepoValidationEx(ex: RepoValidationEx): ErrorResponse =
        ResponseEntity.badRequest().body(ex.errors.allErrors)
}
