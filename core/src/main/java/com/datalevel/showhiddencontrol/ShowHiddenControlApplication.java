package com.datalevel.showhiddencontrol;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.datalevel.showhiddencontrol.core.mapper")
public class ShowHiddenControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShowHiddenControlApplication.class, args);
    }

}
