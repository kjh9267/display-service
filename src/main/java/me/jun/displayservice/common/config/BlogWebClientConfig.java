package me.jun.displayservice.common.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Profile("default")
@Configuration
@LoadBalancerClient(
        name = "BLOG-SERVICE",
        configuration = BlogServiceDiscoveryClientConfig.class
)
public class BlogWebClientConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder blogWebClientBuilder() {
        return WebClient.builder();
    }
}
