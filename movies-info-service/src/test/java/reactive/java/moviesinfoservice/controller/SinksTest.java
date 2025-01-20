package reactive.java.moviesinfoservice.controller;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitFailureHandler;

public class SinksTest {


    @Test
    void sink(){
        Sinks.Many<Integer> replaySink=Sinks.many().replay().all();

        replaySink.emitNext(1, EmitFailureHandler.FAIL_FAST);
        replaySink.emitNext(2, EmitFailureHandler.FAIL_FAST);

        Flux<Integer> integerFlux=replaySink.asFlux();
        integerFlux.subscribe((i)->{
            System.out.println("Subscriber 1: "+i);
        });
        Flux<Integer> integerFlux1=replaySink.asFlux();
        integerFlux1.subscribe((i)->{
            System.out.println("Subscriber 2: "+i);
        });

        replaySink.tryEmitNext(3);

        Flux<Integer> integerFlux2=replaySink.asFlux();
        integerFlux2.subscribe((i)->{
            System.out.println("Subscriber 3: "+i);
        });
    }

    @Test
    void sinks_multicast(){
       Sinks.Many<Integer> multicast=Sinks.many().multicast().onBackpressureBuffer();

        multicast.emitNext(1, EmitFailureHandler.FAIL_FAST);
        multicast.emitNext(2, EmitFailureHandler.FAIL_FAST);

        Flux<Integer> integerFlux=multicast.asFlux();
        integerFlux.subscribe((i)->{
            System.out.println("Subscriber 1: "+i);
        });

        Flux<Integer> integerFlux1=multicast.asFlux();
        integerFlux1.subscribe((i)->{
            System.out.println("Subscriber 2: "+i);
        });

        multicast.emitNext(3, EmitFailureHandler.FAIL_FAST);

    }

    @Test
    void sinks_unicast(){
        Sinks.Many<Integer> multicast=Sinks.many().unicast().onBackpressureBuffer();

        multicast.emitNext(1, EmitFailureHandler.FAIL_FAST);
        multicast.emitNext(2, EmitFailureHandler.FAIL_FAST);

        Flux<Integer> integerFlux=multicast.asFlux();
        integerFlux.subscribe((i)->{
            System.out.println("Subscriber 1: "+i);
        });

        Flux<Integer> integerFlux1=multicast.asFlux();
        integerFlux1.subscribe((i)->{
            System.out.println("Subscriber 2: "+i);
        });

        multicast.emitNext(3, EmitFailureHandler.FAIL_FAST);

    }
}
