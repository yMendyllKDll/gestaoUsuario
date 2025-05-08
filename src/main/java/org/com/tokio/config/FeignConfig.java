package org.com.tokio.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
 
@Configuration
@EnableFeignClients(basePackages = "org.com.tokio.client")
public class FeignConfig {
} 