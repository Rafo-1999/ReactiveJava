package reactive.java.moviesinfoservice.exceptionHandler;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

@ControllerAdvice

public class GlobalErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleResponseBodyError(WebExchangeBindException ex) {
        log.error("Exception Caught in handleRequestBodyError {}",ex.getMessage(),ex);
        var error = ex.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .sorted()
            .collect(Collectors.joining(","));

        log.error("Error is {}", error);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
