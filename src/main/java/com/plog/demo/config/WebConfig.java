package com.plog.demo.config;

import com.plog.demo.config.interceptor.LogInInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LogInInterceptor logInInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("https://picturewithlog.com", "http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/sign-api/signin", "/sign-api/signup", "/sign-api/refresh")
                .excludePathPatterns("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/payment/success", "/payment/fail", "/payment/cancel", "/confirm/sendEmail", "/confirm/checkAuthNumber","/user/checkemail","/user/changePwd");
    }
}
