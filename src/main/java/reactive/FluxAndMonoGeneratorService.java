package reactive;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Validation;
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
    public Flux<String>namesFluxFlatMap(){
        return Flux.fromIterable(List.of("alex","chloe"))
            .map(String::toUpperCase)
            .flatMap(this::splitStringForFluxFlatMapExample)
            .log();
    }

    public Flux<String>namesFluxTransform(int stringLength){

        Function<Flux<String>,Flux<String>>filterMap=name->name.map(String::toUpperCase)
            .filter(s->s.length()>stringLength);

        return Flux.fromIterable(List.of("alex","chloe"))
            .transform(filterMap)
            .flatMap(this::splitStringForFluxFlatMapExample)
            .log();
    }

    public Flux<String>namesFluxFlatMapAsync(){
        return Flux.fromIterable(List.of("alex","chloe"))
            .map(String::toUpperCase)
            .flatMap(this::splitStringForFluxAsyncFlatMapExample)
            .log();
    }
    public Flux<String>namesFluxConcatMap(){
        return Flux.fromIterable(List.of("alex","chloe"))
            .map(String::toUpperCase)
            .concatMap(this::splitStringForFluxAsyncFlatMapExample)
            .log();
    }

    public Flux<String> splitStringForFluxFlatMapExample(String name){
        var charArray=name.split("");
        return Flux.fromArray(charArray);
    }
    public Flux<String> splitStringForFluxAsyncFlatMapExample(String name){
        var charArray=name.split("");
        return Flux.fromArray(charArray).delayElements(Duration.ofMillis(1000));
    }

    public Flux<String>namesFluxImmutability(){
        var namesFlux= Flux.fromIterable(List.of("alex","ben","chloe"));

        namesFlux.map(String::toUpperCase);

        return namesFlux;

    }

    public Mono<String> namesMono(){
        return Mono.just("alex")
            .log();
    }


    public Mono<String> namesMonoMap(){
        return Mono.just("alex")
            .map(String::toUpperCase)
            .log();
    }


    public Mono<String> namesMonoMapFilter(){
        return Mono.just("alex")
            .map(String::toUpperCase)
            .filter(s->s.length()>3)
            .log();
    }

    public Mono<List<String>> namesMonoFlatMap(){
        return Mono.just("alex")
            .map(String::toUpperCase)
            .filter(s -> s.length()>3)
            .flatMap(this::splitStringForMonoFlatMapExample)
            .log();
    }

    public Flux<String> namesMonoFlatMapMany(){
        return Mono.just("alex")
            .map(String::toUpperCase)
            .filter(s -> s.length()>3)
            .flatMapMany(this::splitStringForFluxFlatMapExample)
            .log();
    }

    private Mono<List<String>> splitStringForMonoFlatMapExample(String s) {
        var charArray=s.split("");
        return Mono.just(List.of(charArray));
    }

}
