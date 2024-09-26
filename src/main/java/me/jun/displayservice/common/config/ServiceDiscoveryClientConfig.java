package me.jun.displayservice.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ServiceDiscoveryClientConfig {

    private final DiscoveryClient discoveryClient;

    @Bean
    public ServiceInstanceListSupplier guestbookServiceInstanceListSupplier() {
        return new GuestbookServiceInstanceListSupplier(
                Arrays.asList("BLOG-SERVICE", "GUESTBOOK-SERVICE")
        );
    }

    private class GuestbookServiceInstanceListSupplier implements ServiceInstanceListSupplier {

        private final List<String> serviceIds;

        private GuestbookServiceInstanceListSupplier(List<String> serviceIds) {
            this.serviceIds = serviceIds;
        }

        @Override
        public String getServiceId() {
            return "";
        }

        @Override
        public Flux<List<ServiceInstance>> get() {
            List<ServiceInstance> instances = new ArrayList<>();

            for (String serviceId : serviceIds) {
                instances.addAll(discoveryClient.getInstances(serviceId));
            }

            return Flux.just(instances);
        }
    }
}
