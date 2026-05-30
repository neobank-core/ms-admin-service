package com.neobank.adminservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsAdminServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsAdminServiceApplication.class, args);
    }

}
