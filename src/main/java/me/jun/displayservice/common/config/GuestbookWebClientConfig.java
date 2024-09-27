package me.jun.displayservice.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Profile("default")
@LoadBalancerClient(
        name = "GUESTBOOK-SERVICE",
        configuration = ServiceDiscoveryClientConfig.class
)
@Configuration
@RequiredArgsConstructor
public class GuestbookWebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder guestbookWebClientBuilder() {
        return WebClient.builder();
    }
}
