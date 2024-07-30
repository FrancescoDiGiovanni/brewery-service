package nl.sudsandbuds.brewery_service.handlers;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import nl.sudsandbuds.exceptions.ServiceHttpStatusException;
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
public class GlobalExceptionHandler {

    /**
     * Exception handler, it gets triggered everytime a methods throws a HandlerMethodValidationException
     * @param ex : HandlerMethodValidationException - Exception containing info about validation errors
     * @return errors : Map<String, String> - Map contaning a list of validation errors
     */
    @ExceptionHandler(ServiceHttpStatusException.class)
    public <T> ResponseEntity<Response<T>> handleServiceExceptions(
            ServiceHttpStatusException ex) {
        return ResponseUtility.buildErrorResponseEntityFromServiceException(ex, log);
    }

    @ExceptionHandler(FeignException.class)
    public <T> ResponseEntity<Response<T>> handleFeignExceptions(
            FeignException ex ) {
        return ResponseUtility.buildResponseEntityFromFeignClientException(ex, "BREVERY@", log);
    }
}
