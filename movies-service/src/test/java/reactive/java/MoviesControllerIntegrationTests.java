package reactive.java;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.tomakehurst.wiremock.client.WireMock;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactive.java.moviesservice.MoviesServiceApplication;
import reactive.java.moviesservice.domain.Movie;

@SpringBootTest(classes = MoviesServiceApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8084)
@TestPropertySource(properties = {
    "restClient.moviesInfoUrl=http://localhost:8084/v1/movieinfos",
    "restClient.reviewsUrl=http://localhost:8084/v1/reviews"
})
public class MoviesControllerIntegrationTests {

  @Autowired
  WebTestClient webTestClient;

  @Test
  void retrieveMovieById(){
    var movieId="abc";
    stubFor(WireMock.get(urlEqualTo("/v1/movieinfos"+"/"+movieId))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("movieinfo.json")));

    stubFor(WireMock.get(urlPathEqualTo("/v1/reviews"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("reviews.json")));


    webTestClient
        .get()
        .uri("/v1/movies/{id}",movieId)
        .exchange()
        .expectStatus().isOk()
        .expectBody(Movie.class)
        .consumeWith(movieEntityExchangeResult -> {
          var movie=movieEntityExchangeResult.getResponseBody();
          assert Objects.requireNonNull(movie).getReviews().size()==2;
          assertEquals("Batman Begins",movie.getMovieInfo());
        });
  }
  @Test
  void retrieveMovieById_404(){
    var movieId="abc";
    stubFor(WireMock.get(urlEqualTo("/v1/movieinfos"+"/"+movieId))
        .willReturn(aResponse()
            .withStatus(404)
           ));

    stubFor(WireMock.get(urlPathEqualTo("/v1/reviews"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("reviews.json")));


    webTestClient
        .get()
        .uri("/v1/movies/{id}",movieId)
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody(String.class)
        .isEqualTo("There is no MovieInfo Available for the passed in Id : abc");
  }

  @Test
  void retrieveMovieById_reviews_404(){
    var movieId="abc";
    stubFor(WireMock.get(urlEqualTo("/v1/movieinfos"+"/"+movieId))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("movieinfo.json")));

    stubFor(WireMock.get(urlPathEqualTo("/v1/reviews"))
        .willReturn(aResponse()
            .withStatus(404)));


    webTestClient
        .get()
        .uri("/v1/movies/{id}",movieId)
        .exchange()
        .expectStatus().isOk()
        .expectBody(Movie.class)
        .consumeWith(movieEntityExchangeResult -> {
          var movie=movieEntityExchangeResult.getResponseBody();
          assert Objects.requireNonNull(movie).getReviews().size()==0;
          assertEquals("Batman Begins",movie.getMovieInfo());
        });
  }


  @Test
  void retrieveMovieById_5XX(){
    var movieId="Batman Begins";
    stubFor(WireMock.get(urlEqualTo("/v1/movieinfos"+"/"+movieId))
        .willReturn(aResponse()
            .withStatus(500)
            .withBody("MovieInfo Service Unavailable")
        ));

//    stubFor(WireMock.get(urlPathEqualTo("/v1/reviews"))
//        .willReturn(aResponse()
//            .withHeader("Content-Type", "application/json")
//            .withBodyFile("reviews.json")));


    webTestClient
        .get()
        .uri("/v1/movies/{id}",movieId)
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody(String.class)
        .isEqualTo("MovieInfo Service Unavailable");

    WireMock.verify(4,getRequestedFor(WireMock.urlPathEqualTo("/v1/movieinfos"+"/"+movieId)));
  }

    @Test
    void retrieveMovieById_Reviews_5XX(){
        var movieId="abc";
        stubFor(WireMock.get(urlEqualTo("/v1/movieinfos"+"/"+movieId))
                    .willReturn(aResponse()
                                    .withHeader("Content-Type", "application/json")
                                    .withBodyFile("movieinfo.json")));

        stubFor(WireMock.get(urlEqualTo("/v1/movieinfos"+"/"+movieId))
                    .willReturn(aResponse()
                                    .withStatus(500)
                                    .withBody("Review Service Not Available")
                    ));

        //    stubFor(WireMock.get(urlPathEqualTo("/v1/reviews"))
        //        .willReturn(aResponse()
        //            .withHeader("Content-Type", "application/json")
        //            .withBodyFile("reviews.json")));


        webTestClient
            .get()
            .uri("/v1/movies/{id}",movieId)
            .exchange()
            .expectStatus()
            .is5xxServerError()
            .expectBody(String.class)
            .isEqualTo("Server Exception in ReviewService Review Service Not Available");

        WireMock.verify(4,getRequestedFor(urlPathMatching(("/v1/reviews*"))));
    }
}
