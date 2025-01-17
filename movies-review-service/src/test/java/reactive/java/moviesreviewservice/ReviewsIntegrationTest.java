package reactive.java.moviesreviewservice;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactive.java.moviesreviewservice.domain.Review;
import reactive.java.moviesreviewservice.repository.ReviewReactiveRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class ReviewsIntegrationTest {

    @Autowired
    WebTestClient webClient;

    @Autowired
    ReviewReactiveRepository reviewRepository;

    static  String REVIEWS_PATH="/v1/reviews";

    @BeforeEach
    void setUp() {

        var reviewList= List.of(
            new Review(null,1L,"AwesomeMovie",9.0),
            new Review(null, 1L, "AwesomeMovie1", 9.0),
            new Review(null,2L,"Excellent movie",8.0));
        reviewRepository.saveAll(reviewList).blockLast();
    }

    @AfterEach
    void tearDown() {
        reviewRepository.deleteAll().block();
    }

    @Test
    void addReview(){

        var reviews= new Review(null,1L,"AwesomeMovie",9.0);


        webClient
            .post()
            .uri(REVIEWS_PATH)
            .bodyValue(reviews)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Review.class)
            .consumeWith(movieReviewResult->{
                var savedReview=movieReviewResult.getResponseBody();
                assert savedReview!=null;
                assert savedReview.getReviewId()!=null;
            });
    }

}
