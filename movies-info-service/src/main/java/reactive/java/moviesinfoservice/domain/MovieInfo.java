package reactive.java.moviesinfoservice.domain;

import java.time.LocalDate;
import java.util.List;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@Document
public class MovieInfo {

    @Id
    private String movieInfoId;
    private String name;
    private Integer year;
    private List<String> cast;
    private LocalDate releaseDate;

    public MovieInfo(String movieInfoId, String name, Integer year, List<String> cast, LocalDate releaseDate) {
        this.movieInfoId = movieInfoId;
        this.name = name;
        this.year = year;
        this.cast = cast;
        this.releaseDate = releaseDate;
    }

}
