package reactive.java.moviesreviewservice.handler;

import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactive.java.moviesreviewservice.domain.Review;
import reactive.java.moviesreviewservice.exception.ReviewDataException;
import reactive.java.moviesreviewservice.exception.ReviewNotFoundException;
import reactive.java.moviesreviewservice.repository.ReviewReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class ReviewHandler {


    private final ReviewReactiveRepository reviewReactiveRepository;
    private  Validator validator;
    private static final Logger log = LoggerFactory.getLogger(ReviewHandler.class);

    Sinks.Many<Review> reviewSinks= Sinks.many().replay().latest();

    public ReviewHandler(ReviewReactiveRepository reviewReactiveRepository){
        this.reviewReactiveRepository = reviewReactiveRepository;
    }
    public Mono<ServerResponse> addReview(ServerRequest request) {

        return request.bodyToMono(Review.class)
            .doOnNext(this::validate)
            .flatMap(reviewReactiveRepository::save)
            .doOnNext(review->{
                reviewSinks.tryEmitNext(review);
            })
            .flatMap(savedReview ->
                ServerResponse.status(HttpStatus.CREATED).bodyValue(savedReview)
            );
    }

    private void validate(Review review) {
        var constraintViolations = validator.validate(review);
        log.info("constraintViolations: {}", constraintViolations);
        if (constraintViolations.size()>0) {
            var errorMessage=constraintViolations
                .stream()
                .map(ConstraintViolation::getMessage)
                .sorted()
                .collect(Collectors.joining(","));
            throw  new ReviewDataException(errorMessage);
        }
    }

    public Mono<ServerResponse> getReviews(ServerRequest request) {

        var  movieInfoId=request.queryParam("movieInfoId");

        Flux<Review> reviewsFlux;
        if (movieInfoId.isPresent()){
            reviewsFlux = reviewReactiveRepository.findReviewsByMovieInfoId(Long.valueOf(movieInfoId.get()));
        }else {
            reviewsFlux = reviewReactiveRepository.findAll();
        }
        return buildReviewsResponse(reviewsFlux);

    }

    private static Mono<ServerResponse> buildReviewsResponse(Flux<Review> reviewsFlux) {
        return ServerResponse.ok().body(reviewsFlux, Review.class);
    }

    public Mono<ServerResponse> updateReview(ServerRequest serverRequest) {

        var reviewId= serverRequest.pathVariable("id");

        var existingReview = reviewReactiveRepository.findById(reviewId)
            .switchIfEmpty(Mono.error(new ReviewNotFoundException("Review not found for the given review id"+reviewId)));
            ;

        return existingReview
            .flatMap(review -> serverRequest.bodyToMono(Review.class)
            .map(reqReview->{
                review.setComment(reqReview.getComment());
                review.setRating(reqReview.getRating());
                return review;
                 })
                .flatMap(reviewReactiveRepository::save)
                .flatMap(savedReview->ServerResponse.ok().bodyValue(savedReview)));
    }

    public Mono<ServerResponse> deleteReview(ServerRequest serverRequest) {

        var reviewId=serverRequest.pathVariable("id");

        var existingReview = reviewReactiveRepository.findById(reviewId);

        return existingReview
            .flatMap(review -> reviewReactiveRepository.deleteById(reviewId))
            .then(ServerResponse.noContent().build());
    }

  public Mono<ServerResponse> getReviewsStream(ServerRequest serverRequest) {

        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_NDJSON)
            .body(reviewSinks.asFlux(),Review.class)
            .log();
  }
}
