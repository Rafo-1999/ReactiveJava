package reactive.java.moviesreviewservice;


import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactive.java.moviesreviewservice.domain.Review;
import reactive.java.moviesreviewservice.exceptionHandler.GlobalErrorHandler;
import reactive.java.moviesreviewservice.handler.ReviewHandler;
import reactive.java.moviesreviewservice.repository.ReviewReactiveRepository;
import reactive.java.moviesreviewservice.router.ReviewRouter;
import reactor.core.publisher.Mono;

@WebFluxTest
@AutoConfigureWebTestClient
@ContextConfiguration(classes = {ReviewRouter.class, ReviewHandler.class, GlobalErrorHandler.class})
public class ReviewsUnitTest {

  static String REVIEWS_PATH = "/v1/reviews";

  @MockitoBean
  private ReviewReactiveRepository reviewReactiveRepository;

  @Autowired
  private WebTestClient webTestClient;


  @Test
  void addReview() {
    var reviews = new Review(null, 1L, "AwesomeMovie", 9.0);

    when(reviewReactiveRepository.save(isA(Review.class)))
        .thenReturn(Mono.just(new Review("abc", 1L, "AwesomeMovie", 9.0)));

    webTestClient
        .post()
        .uri(REVIEWS_PATH)
        .bodyValue(reviews)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody(Review.class)
        .consumeWith(movieReviewResult -> {
          var savedReview = movieReviewResult.getResponseBody();
          assert savedReview != null;
          assert savedReview.getReviewId() != null;
        });
  }


  @Test
  void addReviewValidation() {
    var reviews = new Review(null, null, "AwesomeMovie", -9.0);

    when(reviewReactiveRepository.save(isA(Review.class)))
        .thenReturn(Mono.just(new Review("abc", 1L, "AwesomeMovie", 9.0)));

    webTestClient
        .post()
        .uri(REVIEWS_PATH)
        .bodyValue(reviews)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody(String.class)
        .isEqualTo("rating.movieInfoId: must not be null,rating.negative  : please pass a non-negative value");

  }
}
