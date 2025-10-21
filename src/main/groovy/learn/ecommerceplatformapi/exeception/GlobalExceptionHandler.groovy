// Xử lý exception toàn cục
package learn.ecommerceplatformapi.exeception

import learn.ecommerceplatformapi.dto.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException)
    ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = [:]
        ex.bindingResult.allErrors.each { error ->
            String fieldName = ((FieldError) error).field
            String ErrorResponse = error.defaultMessage
            errors[fieldName] = ErrorResponse
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors)
    }

    @ExceptionHandler(AccessDeniedException)
    ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(ex.message ?: 'ACCESS DENIED'))
    }

    @ExceptionHandler(UsernameNotFoundException)
    ResponseEntity<ErrorResponse> handleUserNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.message ?: 'USERNAME NOT FOUND'))
    }

    @ExceptionHandler(RuntimeException)
    ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.message ?: 'BAD REQUEST'))
    }

    @ExceptionHandler(TokenRefreshException)
    ResponseEntity<ErrorResponse> handleTokenRefreshException(TokenRefreshException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(ex.message ?: 'TOKEN REFRESH FAILED'))
    }

    @ExceptionHandler(NoSuchElementException)
    ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.message ?: 'ELEMENT NOT FOUND'))
    }

    @ExceptionHandler(IllegalArgumentException)
    ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.message ?: 'INVALID ARGUMENT'))
    }

    @ExceptionHandler(UsernameOrEmailAlreadyExistsException)
    ResponseEntity<ErrorResponse> handleUsernameOrEmailAlreadyExistsException(UsernameOrEmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.message ?: 'USERNAME OR EMAIL ALREADY EXISTS'))
    }
}
