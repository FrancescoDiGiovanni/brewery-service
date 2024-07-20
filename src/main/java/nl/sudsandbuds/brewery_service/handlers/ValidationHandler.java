package nl.sudsandbuds.brewery_service.handlers;

import lombok.extern.slf4j.Slf4j;
import nl.sudsandbuds.utilities.Response;
import nl.sudsandbuds.utilities.ResponseUtility;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ValidationHandler {

    /**
     * Exception handler, it gets triggered everything a methods throws a HandlerMethodValidationException
     * @param ex : HandlerMethodValidationException - Exception containing info about validation errors
     * @return errors : Map<String, String> - Map contaning a list of validation errors
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Response<Map<String, String>>> handleValidationExceptions(
            HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        List<ParameterValidationResult> validationErrors = ex.getAllValidationResults();
        for (ParameterValidationResult validationError : validationErrors) {
            for (MessageSourceResolvable messageSourceResolvable : validationError.getResolvableErrors() )
                errors.put(validationError.getMethodParameter().getParameterName(), messageSourceResolvable.getDefaultMessage());
        }
        return ResponseUtility.buildErrorResponseEntity("400", HttpStatus.BAD_REQUEST, "Error during validation", errors, log);
    }
}
