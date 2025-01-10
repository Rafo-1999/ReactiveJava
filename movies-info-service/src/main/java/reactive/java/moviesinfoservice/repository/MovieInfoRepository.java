package reactive.java.moviesinfoservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactive.java.moviesinfoservice.domain.MovieInfo;
import reactor.core.publisher.Flux;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {

    Flux<MovieInfo> findByYear(Integer year);
}
