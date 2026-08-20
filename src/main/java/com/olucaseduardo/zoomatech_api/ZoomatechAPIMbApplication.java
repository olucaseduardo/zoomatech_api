package com.olucaseduardo.zoomatech_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ZoomatechAPIMbApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZoomatechAPIMbApplication.class, args);
    }

}
