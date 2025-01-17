package reactive.java.moviesreviewservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = "reactive.java.moviesreviewservice.repository")
public class MoviesReviewServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviesReviewServiceApplication.class, args);
    }
}
