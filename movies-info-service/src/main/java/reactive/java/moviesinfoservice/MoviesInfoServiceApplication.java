package reactive.java.moviesinfoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;


@SpringBootApplication(scanBasePackages = "reactive.java.moviesinfoservice")
@EnableReactiveMongoRepositories(basePackages = "reactive.java.moviesinfoservice.repository")
public class MoviesInfoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesInfoServiceApplication.class, args);
	}

}
