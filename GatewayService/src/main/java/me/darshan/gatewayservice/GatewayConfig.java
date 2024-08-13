package me.darshan.gatewayservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("http://localhost:8081/products")
    private String productServiceUrl;

    @Value("${order.service.url}")
    private String orderServiceUrl;

    @Value("${admin.service.url}")
    private String adminServiceUrl;

    @Value("${image.service.url}")
    private String imageServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("products", r -> r.path("/products/**")
                        .uri(productServiceUrl))
                .route("orders", r -> r.path("/orders/**")
                        .uri(orderServiceUrl))
                .route("admin", r -> r.path("/admin/**")
                        .uri(adminServiceUrl))
                .route("images", r -> r.path("/images/**")
                        .uri(imageServiceUrl))
                .build();
    }
}