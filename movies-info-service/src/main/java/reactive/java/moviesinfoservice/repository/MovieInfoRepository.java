package reactive.java.moviesinfoservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactive.java.moviesinfoservice.domain.MovieInfo;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {

}
