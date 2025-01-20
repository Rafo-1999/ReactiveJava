package reactive.java.moviesreviewservice.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactive.java.moviesreviewservice.handler.ReviewHandler;

@Configuration
public class ReviewRouter {

  @Bean
  public RouterFunction<ServerResponse> reviewsRouter(ReviewHandler reviewHandler) {
    return route()
        .nest(path("/v1/reviews"),builder ->
            builder
                .POST("" ,reviewHandler::addReview)
                .GET("",reviewHandler::getReviews)
                .PUT("/{id}",reviewHandler::updateReview)
                .DELETE("/{id}",reviewHandler::deleteReview)
                .GET("/stream",reviewHandler::getReviewsStream)
            )
        .GET("/v1/helloWorld" ,(request -> ServerResponse.ok().bodyValue("Hello World")))
        .POST("/v1/reviews" ,reviewHandler::addReview)
        .GET("/v1/reviews",reviewHandler::getReviews)
        .build();

  }
}
