package com.example.springboot.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("file:${app.config-home}/aop/aop-context.xml")
public class AopConfig {

}
