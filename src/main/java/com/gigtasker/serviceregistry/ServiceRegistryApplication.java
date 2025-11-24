package com.gigtasker.serviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {

    private ServiceRegistryApplication() {}

    static void main(String[] args) {
        SpringApplication.run(ServiceRegistryApplication.class, args);
    }
}
