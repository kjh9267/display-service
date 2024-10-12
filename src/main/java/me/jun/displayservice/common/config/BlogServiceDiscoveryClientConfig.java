package me.jun.displayservice.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class BlogServiceDiscoveryClientConfig {

    private final DiscoveryClient discoveryClient;

    @Bean
    public ServiceInstanceListSupplier blogServiceInstanceListSupplier() {
        return new BlogServiceInstanceListSupplier("BLOG-SERVICE");
    }

    private class BlogServiceInstanceListSupplier implements ServiceInstanceListSupplier {

        private final String serviceId;

        public BlogServiceInstanceListSupplier(String serviceId) {
            this.serviceId = serviceId;
        }

        @Override
        public String getServiceId() {
            return serviceId;
        }

        @Override
        public Flux<List<ServiceInstance>> get() {
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);

            return Flux.just(instances);
        }
    }
}
