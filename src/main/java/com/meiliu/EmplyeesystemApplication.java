package com.meiliu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.meiliu.dao")
public class EmplyeesystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmplyeesystemApplication.class, args);
    }

}
