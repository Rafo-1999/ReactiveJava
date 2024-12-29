package reactive.java.moviesinfoservice.controller;

import static org.junit.Assert.assertEquals;

import java.util.Objects;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebTestClient
public class FluxAndMonoControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void flux() {
    webTestClient.get()
        .uri("/flux")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBodyList(Integer.class)
        .hasSize(3);
  }

  @Test
  public void flux_approach2() {
     var flux = webTestClient.get()
        .uri("/flux")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .returnResult(Integer.class)
        .getResponseBody();

     StepVerifier.create(flux)
         .expectNext(1, 2, 3)
         .verifyComplete();

  }

  @Test
  public void flux_approach3() {
    var flux = webTestClient.get()
        .uri("/flux")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
            .expectBodyList(Integer.class)
                .consumeWith(listEntityExchangeResult -> {
                  var responseBody=listEntityExchangeResult.getResponseBody();
                  assert (Objects.requireNonNull(responseBody).size()==3);
                });

  }


  @Test
  public void mono() {
    webTestClient.get()
        .uri("/mono")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody(String.class)
        .consumeWith(stringEntityExchangeResult -> {
          var responseBody=stringEntityExchangeResult.getResponseBody();
          assertEquals("hello-world",responseBody);
        });
  }

  @Test
  public void stream() {
   var flux = webTestClient.get()
        .uri("/stream")
        .accept(MediaType.TEXT_EVENT_STREAM)
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .returnResult(Long.class)
        .getResponseBody();

    StepVerifier.create(flux)
        .expectNext(0L, 1L, 2L,3L,4L,5L)
        .thenCancel()
        .verify();
  }
}
