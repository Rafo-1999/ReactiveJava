package reactive.java.moviesreviewservice.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Document
public class Review {

    public Review(String reviewId, Long movieInfoId, String comment, Double rating) {
        this.reviewId = reviewId;
        this.movieInfoId = movieInfoId;
        this.comment = comment;
        this.rating = rating;
    }

    @Id
    private String reviewId;

    @NotNull(message = "rating.movieInfoId: must not be null")
    private Long movieInfoId;

    private String comment;

    @Min(value = 0L, message = "rating.negative  : please pass a non-negative value")
    private Double rating;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public Long getMovieInfoId() {
        return movieInfoId;
    }

    public void setMovieInfoId(Long movieInfoId) {
        this.movieInfoId = movieInfoId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

}
