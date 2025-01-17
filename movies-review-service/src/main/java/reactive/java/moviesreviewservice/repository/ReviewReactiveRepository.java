package reactive.java.moviesreviewservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactive.java.moviesreviewservice.domain.Review;
import reactor.core.publisher.Flux;

public interface ReviewReactiveRepository extends ReactiveMongoRepository<Review, String> {


    Flux<Review> findReviewsByMovieInfoId(Long movieInfoId);
}
