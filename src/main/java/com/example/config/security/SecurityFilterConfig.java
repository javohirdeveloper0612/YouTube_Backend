package com.example.config.security;

import com.example.config.jwt.TokenFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityFilterConfig {
    private final TokenFilter tokenFilter;

    public SecurityFilterConfig(TokenFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean() {

        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(tokenFilter);
        bean.addUrlPatterns("/profile/admin*");
        bean.addUrlPatterns("/article_type/*");
        bean.addUrlPatterns("/region/*");
        bean.addUrlPatterns("/email_history/*");
        bean.addUrlPatterns("/article/admin/*");
        bean.addUrlPatterns("/article_like/*");
        bean.addUrlPatterns("/saved_article/*");
        bean.addUrlPatterns("/comment/sec/*");
        bean.addUrlPatterns("/comment_like/*");
        return bean;


    }
}
