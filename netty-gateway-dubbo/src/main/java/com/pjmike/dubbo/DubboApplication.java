package com.pjmike.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
/**
 * @author pjmike
 */
@EnableAutoConfiguration
public class DubboApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboApplication.class);
    }
}
