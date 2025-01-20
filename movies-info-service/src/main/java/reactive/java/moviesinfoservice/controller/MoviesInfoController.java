package reactive.java.moviesinfoservice.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactive.java.moviesinfoservice.domain.MovieInfo;
import reactive.java.moviesinfoservice.service.MoviesInfoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/v1")
public class MoviesInfoController {

  private final MoviesInfoService moviesInfoService;
  private static final Logger log = LoggerFactory.getLogger(MoviesInfoController.class);

  Sinks.Many<MovieInfo> moviesInfoSink=Sinks.many().replay().all();

  public MoviesInfoController(MoviesInfoService moviesInfoService) {
    this.moviesInfoService = moviesInfoService;
  }

  @GetMapping("/movieinfos")
  public Flux<MovieInfo> getAllMovieInfos(@RequestParam(value = "year",required = false)Integer year) {
    log.info("Year is {}", year);

    if (year != null) {
      return moviesInfoService.getMovieInfoByYear(year);
    }

    return  moviesInfoService.getAllMovieInfos().log();
  }

  @GetMapping("/movieinfos/{id}")
  public Mono<MovieInfo> getAllMovieInfosById(@PathVariable String id) {
    return  moviesInfoService.getAllMovieInfosById(id);
  }
  @GetMapping(value = "/movieinfos/stream",produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Flux<MovieInfo> getMovieInfoById() {
    return moviesInfoSink.asFlux();
  }


  @PostMapping("/movieinfos")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
    System.out.println("Received a request to add movie info");

    return moviesInfoService.addMovieInfo(movieInfo)
        .doOnNext(savedInfo->moviesInfoSink.tryEmitNext(savedInfo));
  }



  @PutMapping("/movieinfos/{id}")
  public Mono<ResponseEntity<MovieInfo>> updateMovieInfo(@RequestBody MovieInfo updatedMovieInfo, @PathVariable String id) {
    return  moviesInfoService.updateMovieInfo(updatedMovieInfo,id)
        .map(ResponseEntity.ok()::body)
        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
        .log();
  }

  @DeleteMapping("/movieinfos/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteMovieInfo(@PathVariable String id) {
    return  moviesInfoService.deleteMovieInfo(id);
  }
}
