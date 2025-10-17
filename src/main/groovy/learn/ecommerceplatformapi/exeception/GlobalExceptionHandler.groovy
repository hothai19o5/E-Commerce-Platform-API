// Xử lý exception toàn cục
package learn.ecommerceplatformapi.exeception

import learn.ecommerceplatformapi.dto.response.MessageResponse
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
        Map<String, String> errors = [:]
        ex.bindingResult.allErrors.each { error ->
            String fieldName = ((FieldError) error).field
            String errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage
        }
        Map<String, Object> body = [message: 'Validation failed', errors: errors]
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    @ExceptionHandler(AccessDeniedException)
    ResponseEntity<MessageResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse('Error: Access denied'))
    }

    @ExceptionHandler(UsernameNotFoundException)
    ResponseEntity<MessageResponse> handleUserNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(ex.message ?: 'User not found'))
    }

    @ExceptionHandler(RuntimeException)
    ResponseEntity<MessageResponse> handleRuntime(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(ex.message ?: 'Bad request'))
    }
}
