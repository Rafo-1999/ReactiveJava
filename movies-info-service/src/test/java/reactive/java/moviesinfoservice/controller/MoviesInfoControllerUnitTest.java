package reactive.java.moviesinfoservice.controller;

import static reactor.core.publisher.Mono.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactive.java.moviesinfoservice.domain.MovieInfo;
import reactive.java.moviesinfoservice.repository.MovieInfoRepository;
import reactive.java.moviesinfoservice.service.MoviesInfoService;
import reactor.core.publisher.Flux;

@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MoviesInfoControllerUnitTest {

    public static final String MOVIES_INFO_PATH = "/v1/movieinfos";

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    MovieInfoRepository movieInfoRepository;
    @Autowired
    private MoviesInfoService moviesInfoServiceMocked;

    @AfterEach
    void cleanUp() {
        movieInfoRepository.deleteAll().block();

    }

    @Test
    void addMovieInfoValidation() {
        var movieinfo= new MovieInfo("1", "", -2008, List.of(""),
                                     LocalDate.parse("2008-07-18"));

        webTestClient
            .post()
            .uri(MOVIES_INFO_PATH)
            .bodyValue(movieinfo)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .consumeWith(result -> {
                var responseBody= result.getResponseBody();
                System.out.println("responseBody = "+ responseBody);
                assert responseBody != null;
            });

    }


    @Test
    void getAllMoviesInfo  (){

        var movieInfos = List.of(
            new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Caine"),
                          LocalDate.parse("2005-06-15")),
            new MovieInfo("abc", "The Dark Knight", 2008, List.of("Christian Bale", "Heath Ledger"),
                          LocalDate.parse("2008-07-18")),
            new MovieInfo(null, "Inception", 2010, List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"),
                          LocalDate.parse("2010-07-16")));

        when(moviesInfoServiceMocked.getAllMovieInfos()).thenReturn(Flux.fromIterable(movieInfos));

        webTestClient
            .get()
            .uri(MOVIES_INFO_PATH)
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBodyList(MovieInfo.class)
            .hasSize(0);
    }
}
