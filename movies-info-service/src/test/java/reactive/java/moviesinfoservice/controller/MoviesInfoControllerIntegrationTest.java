package reactive.java.moviesinfoservice.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactive.java.moviesinfoservice.domain.MovieInfo;
import reactive.java.moviesinfoservice.repository.MovieInfoRepository;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MoviesInfoControllerIntegrationTest {

    public static final String MOVIES_INFO_PATH = "/v1/movieinfos";
    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var movieInfos = List.of(
            new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Caine"),
                          LocalDate.parse("2005-06-15")),
            new MovieInfo("abc", "The Dark Knight", 2008, List.of("Christian Bale", "Heath Ledger"),
                          LocalDate.parse("2008-07-18")),
            new MovieInfo(null, "Inception", 2010, List.of("Leonardo DiCaprio", "Joseph Gordon-Levitt"),
                          LocalDate.parse("2010-07-16")));
        movieInfoRepository.saveAll(movieInfos).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }




    @Test
    void addMovieInfo() {
        var movieinfo= new MovieInfo("1", "The Dark", 2008, List.of("Christian Bale", "Heath Ledger"),
                                    LocalDate.parse("2008-07-18"));

        webTestClient
            .post()
            .uri(MOVIES_INFO_PATH)
            .bodyValue(movieinfo)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(MovieInfo.class)
            .consumeWith(result -> {
                var savedMovieInfo=result.getResponseBody();
                assert savedMovieInfo != null;
                assert savedMovieInfo.getMovieInfoId()!=null;
            });
    }

    @Test
    void getAllMovieInfos() {

        webTestClient
            .get()
            .uri(MOVIES_INFO_PATH)
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBodyList(MovieInfo.class)
            .hasSize(3);
    }

    @Test
    void getAllMovieInfosById() {
        var movieInfoId = "abc";
        webTestClient
            .get()
            .uri(MOVIES_INFO_PATH + "/{id}", movieInfoId)
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody()
            .jsonPath("$.name").isEqualTo("The Dark Knight");
    }

    @Test
    void updateMovieInfo() {
        var movieInfoId = "abc";

        var movieinfo= new MovieInfo("1", "The Dark Knight1", 2008, List.of("Christian Bale", "Heath Ledger"),
                                     LocalDate.parse("2008-07-18"));

        webTestClient
            .put()
            .uri(MOVIES_INFO_PATH + "/{id}", movieInfoId)
            .bodyValue(movieinfo)
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBody(MovieInfo.class)
            .consumeWith(result -> {
                var updatedMovieInfo=result.getResponseBody();
                assert updatedMovieInfo != null;
                assert updatedMovieInfo.getMovieInfoId()!=null;
                assertEquals("The Dark Knight1",updatedMovieInfo.getName());
            });
    }

    @Test
    void deleteMovieInfo() {
        var movieInfoId = "abc";
        webTestClient
            .delete()
            .uri(MOVIES_INFO_PATH + "/{id}", movieInfoId)
            .exchange()
            .expectStatus()
            .isNoContent();

    }


    @Test
    void updateMovieInfoNotFound() {
        var movieInfoId = "def";

        var movieinfo= new MovieInfo("1", "The Dark Knight1", 2008, List.of("Christian Bale", "Heath Ledger"),
                                     LocalDate.parse("2008-07-18"));

        webTestClient
            .put()
            .uri(MOVIES_INFO_PATH + "/{id}", movieInfoId)
            .bodyValue(movieinfo)
            .exchange()
            .expectStatus()
            .isNotFound();

    }

    @Test
    void findByYear(){

        var movieInfoFlux=movieInfoRepository.findByYear(2010).log();

        StepVerifier.create(movieInfoFlux)
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void getAllMovieInfoByYear() {

        var uri = UriComponentsBuilder.fromUriString(MOVIES_INFO_PATH)
            .queryParam("year", "2010")
                .buildAndExpand()
                    .toUri();
        webTestClient
            .get()
            .uri(uri)
            .exchange()
            .expectStatus()
            .is2xxSuccessful()
            .expectBodyList(MovieInfo.class)
            .hasSize(1);
    }


}