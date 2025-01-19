package reactive.java.moviesservice.globalErrorHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactive.java.moviesservice.exception.MoviesInfoClientException;

@ControllerAdvice
public class GlobalErrorHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);


  @ExceptionHandler
  public ResponseEntity<String> handleClientException(MoviesInfoClientException exception) {
    log.error("Exception Caught in handleClientException :{}",exception.getMessage());
    return ResponseEntity.status(exception.getStatusCode()).body(exception.getMessage());
  }
  @ExceptionHandler
  public ResponseEntity<String> handleRuntimeException(RuntimeException exception) {
    log.error("Exception Caught in handleServerException :{}",exception.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }
}
