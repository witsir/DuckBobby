package com.duckbobby.common;

import com.duckbobby.service.IPCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Configuration
public class filterRegistration {


    @Bean
    public FilterRegistrationBean myFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new IPCountFilter());
        registration.setUrlPatterns(Arrays.asList("/*","/"));
//        registration.addInitParameter("paramName", "paramValue");
//        registration.setName("IPCountFilter");
//        registration.setOrder(1);
        return registration;
    }
}
