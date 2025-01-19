package reactive.java.moviesservice.domain;

import java.util.List;

import reactive.java.moviesreviewservice.domain.Review;


public class Movie {

  private MovieInfo movieInfo;
  private List<Review> reviews;


  public Movie(MovieInfo movieInfo, List<Review> reviews) {
    this.movieInfo = movieInfo;
    this.reviews = reviews;
  }

  public MovieInfo getMovieInfo() {
    return movieInfo;
  }

  public void setMovieInfo(MovieInfo movieInfo) {
    this.movieInfo = movieInfo;
  }

  public List<Review> getReviews() {
    return reviews;
  }

  public void setReviews(List<Review> reviews) {
    this.reviews = reviews;
  }
}
