package com.xloya.wenda.configuration;


import com.xloya.wenda.interceptor.PassportIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
public class WenDaWebConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    PassportIntercepter passportIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportIntercepter);
        super.addInterceptors(registry);
    }
}
