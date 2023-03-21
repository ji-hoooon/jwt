package com.mino.jwt.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source =new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        //내 서버 응답시 json을 JS에서 처리할 수 있게 설정

        config.addAllowedOrigin("*");
        //모든 ip에 응답 허용
        config.addAllowedHeader("*");
        //모든 헤더에 응답 허용
        config.addAllowedMethod("*");
        //모든 HTTP메서드 타입 요청을 허용하겠다.
        source.registerCorsConfiguration("/api/**", config);
        //설정파일에 해당 URL 패턴에 설정 소스를 등록

        return new CorsFilter(source);
    }
}
