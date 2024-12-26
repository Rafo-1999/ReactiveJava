package reactive;

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

        int stringLength=3;
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
}