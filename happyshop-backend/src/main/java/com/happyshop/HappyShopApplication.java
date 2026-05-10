package com.happyshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.happyshop.mapper")
public class HappyShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(HappyShopApplication.class, args);
    }
}
