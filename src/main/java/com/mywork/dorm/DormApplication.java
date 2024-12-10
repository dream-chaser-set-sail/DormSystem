package com.mywork.dorm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
@MapperScan("com.mywork.dorm.Mapper")
public class DormApplication {

    public static void main(String[] args) {
        SpringApplication.run(DormApplication.class, args);
    }

}
