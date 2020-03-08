package com.github.tuding.blindbox.configuration;

import com.github.tuding.blindbox.filter.WxAuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    WxAuthorizationInterceptor wxAuthorizationInterceptor;


    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(wxAuthorizationInterceptor);
    }
}
