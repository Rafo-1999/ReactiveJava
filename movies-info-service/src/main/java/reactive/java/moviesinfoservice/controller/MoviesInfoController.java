package reactive.java.moviesinfoservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactive.java.moviesinfoservice.domain.MovieInfo;
import reactive.java.moviesinfoservice.service.MoviesInfoService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class MoviesInfoController {

  private MoviesInfoService moviesInfoService;

  public MoviesInfoController(MoviesInfoService moviesInfoService) {
    this.moviesInfoService = moviesInfoService;
  }

  @PostMapping("/movieinfos")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<MovieInfo> addMovieInfo(@RequestBody MovieInfo movieInfo) {
    System.out.println("Received a request to add movie info");

    return moviesInfoService.addMovieInfo(movieInfo).log();
  }
}
