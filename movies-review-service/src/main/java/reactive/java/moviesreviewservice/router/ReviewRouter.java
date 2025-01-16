package reactive.java.moviesreviewservice.router;

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
        .GET("/v1/helloWorld" ,(request -> ServerResponse.ok().bodyValue("helloworld")))
        .POST("/v1/reviews" ,request->reviewHandler.addReview(request))
        .build();

  }
}
