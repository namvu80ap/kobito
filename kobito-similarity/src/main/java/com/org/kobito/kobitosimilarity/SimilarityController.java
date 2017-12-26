package com.org.kobito.kobitosimilarity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@RestController
public class SimilarityController {
    @GetMapping(value = "/")
    public String index( ) {
        return "OK";
    }

    @GetMapping(value = "/list")
    Flux<int[]> list( ) {

        return Flux.fromIterable( Arrays.asList(IntStream.range(1, 10000).toArray()))
                .delayElements(Duration.ofMillis(100))
                .take(10);

    }


}
