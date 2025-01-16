package reactive.java.moviesreviewservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactive.java.moviesreviewservice.domain.Review;

public interface ReviewReactiveRepository extends ReactiveMongoRepository<Review, String> {

}
