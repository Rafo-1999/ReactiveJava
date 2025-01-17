package reactive.java.moviesreviewservice.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactive.java.moviesreviewservice.domain.Review;
import reactive.java.moviesreviewservice.repository.ReviewReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ReviewHandler {

    private final ReviewReactiveRepository reviewReactiveRepository;

    public ReviewHandler(ReviewReactiveRepository reviewReactiveRepository) {
        this.reviewReactiveRepository = reviewReactiveRepository;
    }

    public Mono<ServerResponse> addReview(ServerRequest request) {

        return request.bodyToMono(Review.class)
            .flatMap(reviewReactiveRepository::save)
            .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);

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

        var existingReview = reviewReactiveRepository.findById(reviewId);

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
}
