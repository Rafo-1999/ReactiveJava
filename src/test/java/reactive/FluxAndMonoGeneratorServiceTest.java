package reactive;

import java.util.List;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();

    @Test
    void nameFluxAndMono() {
        //given

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux();

        //then
        StepVerifier.create(namesFlux)
            .expectNext("alex", "ben", "chloe")
            .expectNextCount(0)
            .verifyComplete();
    }

    @Test
    void namesFluxMap() {
        //given

        int stringLength = 3;
        //when
        var nextFluxMap = fluxAndMonoGeneratorService.namesFluxMap(stringLength);
        //then
        StepVerifier.create(nextFluxMap)
            .expectNext("ALEX", "CHLOE")
            .verifyComplete();
    }

    @Test
    void namesFluxImmutability() {
        //given

        //when
        var nextFluxImmutability = fluxAndMonoGeneratorService.namesFluxImmutability();

        //then
        StepVerifier.create(nextFluxImmutability)
            .expectNext("alex", "ben", "chloe")
            .verifyComplete();
    }

    @Test
    void namesFluxFlatMap() {
        //given

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFluxFlatMap();

        //then
        StepVerifier.create(namesFlux)
            .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
            .verifyComplete();
    }

    @Test
    void namesFluxFlatMapAsync() {
        //given

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFluxFlatMapAsync();

        //then
        StepVerifier.create(namesFlux)
            //.expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
            .expectNextCount(9)
            .verifyComplete();
    }

    @Test
    void namesFluxConcatMap() {

        //given

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFluxConcatMap();

        //then
        StepVerifier.create(namesFlux)
            //.expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
            .expectNextCount(9)
            .verifyComplete();

    }

    @Test
    void namesMonoMap() {
        //given

        //when
        var namesMono=fluxAndMonoGeneratorService.namesMonoMap();
        //then
        StepVerifier.create(namesMono).expectNext("ALEX").verifyComplete();

    }

    @Test
    void namesMonoMapFilter() {
        //given

        //when
        var namesMono=fluxAndMonoGeneratorService.namesMonoMapFilter();

        //then
        StepVerifier.create(namesMono).expectNextCount(1).verifyComplete();
    }

    @Test
    void namesMonoFlatMap() {
        //given

        //when
        var namesMono=fluxAndMonoGeneratorService.namesMonoFlatMap();

        //then
        StepVerifier.create(namesMono)
            .expectNext(List.of("A", "L", "E", "X"))
            .verifyComplete();
    }

    @Test
    void namesMonoFlatMapMany() {
        //given
        int stringLength=3;

        //when
        var namesMono=fluxAndMonoGeneratorService.namesMonoFlatMapMany();

        //then
        StepVerifier.create(namesMono)
            .expectNext("A", "L", "E", "X")
            .verifyComplete();
    }

    @Test
    void namesFluxTransform() {
        //given

        int stringLength=3;
        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFluxTransform(stringLength);

        //then
        StepVerifier.create(namesFlux)
            .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
            .verifyComplete();
    }
}