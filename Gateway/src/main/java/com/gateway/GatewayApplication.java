package com.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *@MapperScan("com.gateway.mapper") 给指定包里面的所有类上面都加了一个@mapper注解
 */
@MapperScan("com.gateway.mapper")
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
