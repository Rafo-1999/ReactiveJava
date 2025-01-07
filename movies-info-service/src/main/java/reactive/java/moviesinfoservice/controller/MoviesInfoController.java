package reactive.java.moviesinfoservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactive.java.moviesinfoservice.domain.MovieInfo;
import reactive.java.moviesinfoservice.service.MoviesInfoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class MoviesInfoController {

  private MoviesInfoService moviesInfoService;

  public MoviesInfoController(MoviesInfoService moviesInfoService) {
    this.moviesInfoService = moviesInfoService;
  }

  @GetMapping("/movieinfos")
  public Flux<MovieInfo> getAllMovieInfos() {
    return  moviesInfoService.getAllMovieInfos().log();
  }

  @GetMapping("/movieinfos/{id}")
  public Mono<MovieInfo> getAllMovieInfosById(@PathVariable String id) {
    return  moviesInfoService.getAllMovieInfosById(id);
  }


  @PostMapping("/movieinfos")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<MovieInfo> addMovieInfo(@RequestBody MovieInfo movieInfo) {
    System.out.println("Received a request to add movie info");

    return moviesInfoService.addMovieInfo(movieInfo).log();
  }

  @PutMapping("/movieinfos/{id}")
  public Mono<MovieInfo> updateMovieInfo(@RequestBody MovieInfo updatedMovieInfo, @PathVariable String id) {
    return  moviesInfoService.updateMovieInfo(updatedMovieInfo,id);
  }

  @DeleteMapping("/movieinfos/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteMovieInfo(@PathVariable String id) {
    return  moviesInfoService.deleteMovieInfo(id);
  }
}
