package reactive.java.moviesservice.util;

import java.time.Duration;

import reactive.java.moviesservice.exception.MoviesInfoServerException;
import reactive.java.moviesservice.exception.ReviewsServerException;
import reactor.core.Exceptions;
import reactor.util.retry.Retry;

public class RetryUtil {

    public static Retry retrySpec(){
       return Retry.fixedDelay(3, Duration.ofSeconds(1))
           .filter(ex->ex instanceof MoviesInfoServerException || ex instanceof ReviewsServerException)
            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure()));

    }


}
