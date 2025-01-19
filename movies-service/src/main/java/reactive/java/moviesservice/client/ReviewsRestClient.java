package reactive.java.moviesservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactive.java.moviesreviewservice.domain.Review;
import reactive.java.moviesservice.exception.MoviesInfoClientException;
import reactive.java.moviesservice.exception.MoviesInfoServerException;
import reactive.java.moviesservice.exception.ReviewsClientException;
import reactive.java.moviesservice.exception.ReviewsServerException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ReviewsRestClient {

  private Logger log = LoggerFactory.getLogger(ReviewsRestClient.class);
  private final WebClient webClient;

  @Value("${restClient.reviewsUrl}")
  private String reviewsUrl;

  public ReviewsRestClient(WebClient webClient) {
    this.webClient = webClient;
  }


  public Flux<Review> retrieveReviews(String movieId) {

    var url= UriComponentsBuilder.fromHttpUrl(reviewsUrl)
        .queryParam("movieInfoId", movieId)
        .buildAndExpand()
        .toUriString();


    return webClient
        .get()
        .uri(url)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
          log.info("Status code is:  {}");
          if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
            return Mono.empty();

          }

          return clientResponse.bodyToMono(String.class)
              .flatMap(responseMessage->Mono.error(new ReviewsClientException(
                  responseMessage
              )));
        })
        .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {

          return clientResponse.bodyToMono(String.class)
              .flatMap(responseMessage->Mono.error(new ReviewsServerException(
                  "Server Exception in ReviewService"+responseMessage
              )));
        })
        .bodyToFlux(Review.class);

  }
}
