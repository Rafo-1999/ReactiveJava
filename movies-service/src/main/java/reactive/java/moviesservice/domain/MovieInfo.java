package reactive.java.moviesservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

import org.springframework.validation.annotation.Validated;

@Validated
public class MovieInfo {

  private String movieInfoId;
  @NotBlank(message = "movieInfo.name must be present")
  private String name;
  @NotNull
  @Positive(message = "movieInfo.year must be Positive Value")
  private Integer year;
  @NotNull
  private List<@NotBlank(message = "movieInfo.cast must be present")String> cast;
  private LocalDate releaseDate;

  public MovieInfo() {
  }

  public MovieInfo(String movieInfoId, String name, Integer year,
      List<@NotBlank(message = "movieInfo.cast must be present") String> cast,
      LocalDate releaseDate) {
    this.movieInfoId = movieInfoId;
    this.name = name;
    this.year = year;
    this.cast = cast;
    this.releaseDate = releaseDate;
  }

  public String getMovieInfoId() {
    return movieInfoId;
  }

  public void setMovieInfoId(String movieInfoId) {
    this.movieInfoId = movieInfoId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public List<String> getCast() {
    return cast;
  }

  public void setCast(List<String> cast) {
    this.cast = cast;
  }

  public LocalDate getReleaseDate() {
    return releaseDate;
  }

  public void setReleaseDate(LocalDate releaseDate) {
    this.releaseDate = releaseDate;
  }
}
