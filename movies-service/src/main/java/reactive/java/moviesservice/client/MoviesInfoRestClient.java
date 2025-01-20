package reactive.java.moviesservice.client;

import java.time.Duration;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactive.java.moviesservice.domain.Movie;
import reactive.java.moviesservice.domain.MovieInfo;
import reactive.java.moviesservice.exception.MoviesInfoClientException;
import reactive.java.moviesservice.exception.MoviesInfoServerException;
import reactive.java.moviesservice.util.RetryUtil;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
public class MoviesInfoRestClient {

  private  WebClient webClient;

  private Logger log= Logger.getLogger(this.getClass().getName());
  @Value("${restClient.moviesInfoUrl}")
  private String moviesInfoUrl;

  public MoviesInfoRestClient(WebClient webClient) {
    this.webClient = webClient;
  }


  public Mono<MovieInfo> retrieveMovieInfo(String movieId) {
    var url = moviesInfoUrl.concat("/{id}");


    return webClient
        .get()
        .uri(url, movieId)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
          log.info("Status code is:  {}");
          if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
            return Mono.error(new MoviesInfoClientException(
                "There is no MovieInfo Available for the passed in Id :" + movieId,
                clientResponse.statusCode().value()));

          }

          return clientResponse.bodyToMono(String.class)
              .flatMap(responseMessage->Mono.error(new MoviesInfoClientException(
                  responseMessage,clientResponse.statusCode().value()
              )));
        })
        .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
          log.info("Status code is:  {}");


          return clientResponse.bodyToMono(String.class)
              .flatMap(responseMessage->Mono.error(new MoviesInfoServerException(
                  "Server Exception in MoviesInfoService"+responseMessage
              )));
        })
        .bodyToMono(MovieInfo.class)
        //.retry(3)
        .retryWhen(RetryUtil.retrySpec())
        .log();
  }


  public Flux<MovieInfo> retrieveMovieInfoStream() {

    var url=moviesInfoUrl.concat("/stream");

    return webClient
        .get()
        .uri(url)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
          log.info("Status code is:  {}");

          return clientResponse.bodyToMono(String.class)
              .flatMap(responseMessage->Mono.error(new MoviesInfoClientException(
                  responseMessage,clientResponse.statusCode().value()
              )));
        })
        .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
          log.info("Status code is:  {}");


          return clientResponse.bodyToMono(String.class)
              .flatMap(responseMessage->Mono.error(new MoviesInfoServerException(
                  "Server Exception in MoviesInfoService"+responseMessage
              )));
        })
        .bodyToFlux(MovieInfo.class)
        //.retry(3)
        .retryWhen(RetryUtil.retrySpec())
        .log();
  }
}
