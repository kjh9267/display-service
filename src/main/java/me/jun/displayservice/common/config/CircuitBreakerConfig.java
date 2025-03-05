package me.jun.displayservice.common.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerConfig {

    @Bean
    public CircuitBreaker blogServiceCircuitBreaker() {
        return CircuitBreakerRegistry.ofDefaults()
                .circuitBreaker("blogServiceCircuitBreaker");
    }

    @Bean
    public CircuitBreaker guestbookServiceCircuitBreaker() {
        return CircuitBreakerRegistry.ofDefaults()
                .circuitBreaker("guestbookServiceCircuitBreaker");
    }
}
