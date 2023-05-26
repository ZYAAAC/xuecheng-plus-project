package com.xuecheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class XuechengPlusContentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(XuechengPlusContentServiceApplication.class, args);
    }

}
