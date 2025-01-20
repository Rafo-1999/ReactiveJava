package reactive.java.moviesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
public class MoviesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviesServiceApplication.class, args);
    }
}
