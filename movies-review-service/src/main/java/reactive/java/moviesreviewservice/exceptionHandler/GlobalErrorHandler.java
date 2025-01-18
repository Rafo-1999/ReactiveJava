package reactive.java.moviesreviewservice.exceptionHandler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactive.java.moviesreviewservice.exception.ReviewDataException;
import reactive.java.moviesreviewservice.exception.ReviewNotFoundException;
import reactor.core.publisher.Mono;

@Component
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    log.error("Exception message is {}", ex.getMessage(),ex);
    DataBufferFactory dataBufferFactory=exchange.getResponse().bufferFactory();
    var errorMessage = dataBufferFactory.wrap(ex.getMessage().getBytes());

    if (ex instanceof ReviewNotFoundException){
      exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
      return exchange.getResponse().writeWith(Mono.just(errorMessage));
    }
    if (ex instanceof ReviewDataException){
      exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
      return exchange.getResponse().writeWith(Mono.just(errorMessage));
    }
    exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    return exchange.getResponse().writeWith(Mono.just(errorMessage));
  }
}
