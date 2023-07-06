package com.example.tuanfei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = {"com.example.tuanfei.mapper"})
public class TuanfeiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TuanfeiApplication.class, args);
    }

}
