package com.example.springcloudloadbalanceruser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Retention;

@SpringBootApplication
@RestController
public class UserApplication {

    private final Logger logger = LoggerFactory.getLogger(UserApplication.class);
    private final RestTemplate restTemplate;

    public UserApplication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

    @GetMapping("/hi")
    public String hi(@RequestParam(value = "name", defaultValue = "Mary") String name) {
        logger.info("Accessing /hi endpoint");
        String greeting = restTemplate.getForObject("http://say-hello/greeting", String.class);
        return greeting + " " + name;
    }

}
