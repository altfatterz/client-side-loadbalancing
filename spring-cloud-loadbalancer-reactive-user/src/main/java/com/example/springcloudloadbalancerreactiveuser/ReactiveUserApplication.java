package com.example.springcloudloadbalancerreactiveuser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class ReactiveUserApplication {

    private final WebClient.Builder loadBalancedWebClientBuilder;
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;
    private final Logger logger = LoggerFactory.getLogger(ReactiveUserApplication.class);

    public ReactiveUserApplication(WebClient.Builder loadBalancedWebClientBuilder,
                                   ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        this.loadBalancedWebClientBuilder = loadBalancedWebClientBuilder;
        this.lbFunction = lbFunction;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveUserApplication.class, args);
    }

    @GetMapping("/hi")
    public Mono<String> hi(@RequestParam(value = "name", defaultValue = "Mary") String name,
                           @CookieValue("sc-lb-instance-id") String instanceId) {

        logger.info("Cookie: sc-lb-instance-id:" + instanceId);

        return loadBalancedWebClientBuilder.build().get().uri("http://say-hello/greeting")
                .retrieve().bodyToMono(String.class)
                .map(greeting -> String.format("%s, %s!", greeting, name));
    }

    @GetMapping("/hello")
    public Mono<String> hello(@RequestParam(value = "name", defaultValue = "John") String name) {
        return WebClient.builder()
                .filter(lbFunction)
                .build().get().uri("http://say-hello/greeting")
                .retrieve().bodyToMono(String.class)
                .map(greeting -> String.format("%s, %s!", greeting, name));
    }

}
