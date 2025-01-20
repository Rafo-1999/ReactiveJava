package reactive.java.moviesservice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactive.java.moviesservice.client.MoviesInfoRestClient;
import reactive.java.moviesservice.client.ReviewsRestClient;
import reactive.java.moviesservice.domain.Movie;
import reactive.java.moviesservice.domain.MovieInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movies")
public class MoviesController {

  private final MoviesInfoRestClient moviesInfoRestClient;
  private final ReviewsRestClient reviewsRestClient;

  public MoviesController(MoviesInfoRestClient moviesInfoRestClient,
      ReviewsRestClient reviewsRestClient) {
    this.moviesInfoRestClient = moviesInfoRestClient;
    this.reviewsRestClient = reviewsRestClient;
  }

  public Mono<Movie> retrieveMovieById(@PathVariable("id")String movieId){
    moviesInfoRestClient.retrieveMovieInfo(movieId)
        .flatMap(movieInfo->{
         var reviewsListMono=  reviewsRestClient.retrieveReviews(movieId)
              .collectList();

         return reviewsListMono.map(
             reviews -> new Movie(movieInfo,reviews));
        });

    return null;
  }


  @GetMapping(value = "/stream",produces = MediaType.APPLICATION_NDJSON_VALUE)
  public Flux<MovieInfo> retrieveMovieById(){
    return moviesInfoRestClient.retrieveMovieInfoStream();
  }
}
