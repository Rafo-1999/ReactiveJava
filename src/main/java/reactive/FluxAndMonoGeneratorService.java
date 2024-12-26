package reactive;

import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoGeneratorService {

    public static void main(String[] args) {

        FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();
        service.namesFlux().subscribe(name -> System.out.println("Name is: " + name ));

        service.namesMono().subscribe(name -> System.out.println("Mono name is: " + name ));

    }


    public Flux<String>namesFlux(){
        return Flux.fromIterable(List.of("alex","ben","chloe")).log();
    }

    public Flux<String>namesFluxMap(int stringLength){
        return Flux.fromIterable(List.of("alex","ben","chloe"))
            .map(String::toUpperCase)
            .filter(s->s.length()>stringLength)
            .log();
    }
    public Flux<String>namesFluxFlatMap(int stringLength){
        return Flux.fromIterable(List.of("alex","ben","chloe"))
            .map(String::toUpperCase)
            .filter(s->s.length()>stringLength)
            .flatMap(this::splitStringForFlatMapExample)
            .log();
    }

    public Flux<String> splitStringForFlatMapExample(String name){
        var charArray=name.split("");
        return Flux.fromArray(charArray);
    }

    public Flux<String>namesFluxImmutability(){
        var namesFlux= Flux.fromIterable(List.of("alex","ben","chloe"));

        namesFlux.map(String::toUpperCase);

        return namesFlux;

    }


    public Mono<String> namesMono(){
        return Mono.just("alex").log();
    }
}
