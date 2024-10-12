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
public class GuestbookServiceDiscoveryClientConfig {

    private final DiscoveryClient discoveryClient;

    @Bean
    public ServiceInstanceListSupplier guestbookServiceInstanceListSupplier() {
        return new GuestbookServiceInstanceListSupplier("GUESTBOOK-SERVICE");
    }

    private class GuestbookServiceInstanceListSupplier implements ServiceInstanceListSupplier {

        private final String serviceId;

        public GuestbookServiceInstanceListSupplier(String serviceId) {
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
