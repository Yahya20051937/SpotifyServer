package org.example.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {
    @Bean(name = "extern")
    public WebClient.Builder externWebClientBuilder(){
        return WebClient.builder();
    }

    @Bean(name = "intern")
    @LoadBalanced
    public WebClient.Builder internWebClientBuilder(){return WebClient.builder();}


}
