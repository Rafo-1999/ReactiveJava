package reactive.java.moviesinfoservice.repository;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import reactive.java.moviesinfoservice.domain.MovieInfo;
import reactor.test.StepVerifier;

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {
        var movieInfos = List.of(
            new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Caine"),
                          LocalDate.parse("2005-06-15")),
            new MovieInfo(null, "The Dark Knight", 2008, List.of("Christian Bale", "Heath Ledger"),
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
    void findAll() {

        var movieInfoFlux=movieInfoRepository.findAll();

        StepVerifier.create(movieInfoFlux)
            .expectNextCount(3)
            .verifyComplete();
    }
}