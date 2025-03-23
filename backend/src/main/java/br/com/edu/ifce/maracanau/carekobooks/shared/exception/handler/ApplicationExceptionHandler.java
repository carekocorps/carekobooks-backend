package br.com.edu.ifce.maracanau.carekobooks.shared.exception.handler;

import br.com.edu.ifce.maracanau.carekobooks.shared.exception.BaseException;
import br.com.edu.ifce.maracanau.carekobooks.shared.exception.response.ExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public final ResponseEntity<Object> handleAbstract(BaseException ex) {
        return handleGeneric(ex, List.of());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = ex.getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        return handleGeneric(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation Error", errors);
    }

    private <T extends Exception> ResponseEntity<Object> handleGeneric(T ex, List<String> errors) {
        var responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        var httpStatus = responseStatus != null
                ? responseStatus.value()
                : HttpStatus.INTERNAL_SERVER_ERROR;

        return handleGeneric(httpStatus.value(), ex.getMessage(), errors);
    }

    private ResponseEntity<Object> handleGeneric(int status, String message, List<String> errors) {
        var response = new ExceptionResponse(status, message, errors);
        return ResponseEntity.status(status).body(response);
    }

}
