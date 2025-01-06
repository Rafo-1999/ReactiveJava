package reactive.java.moviesinfoservice.service;

import org.springframework.stereotype.Service;
import reactive.java.moviesinfoservice.domain.MovieInfo;
import reactive.java.moviesinfoservice.repository.MovieInfoRepository;
import reactor.core.publisher.Mono;

@Service
public class MoviesInfoService {

  private MovieInfoRepository movieInfoRepository;

  public MoviesInfoService(MovieInfoRepository movieInfoRepository) {
    this.movieInfoRepository = movieInfoRepository;
  }

  public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {

   return movieInfoRepository.save(movieInfo);
  }
}
