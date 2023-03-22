package com.mino.jwt.config;

import com.mino.jwt.filter.MyFilter1;
import com.mino.jwt.filter.MyFilter3;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
//시큐리티에 필터 추가를 위한 어노테이션
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final CorsFilter corsFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.addFilter(new MyFilter1());
        http.addFilterBefore(new MyFilter3(), ChannelProcessingFilter.class);
        //가장 먼저 시큐리티 필터의 이전에 걸기위해
//        http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class);
        //시큐리티 필터 이전에 걸기위해
//        http.addFilterAfter(new MyFilter3(), BasicAuthenticationFilter.class);
        //시큐리티 필터 이후에 걸기위해
        http.csrf().disable();
        //jwt을 사용하기 위해 세션을 사용하지 않도록 stateless 서버로 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //작성한 CORS필터 추가
                //: @CrossOrigin(인증X), 시큐리티 필터에 등록(인증O)
                .addFilter(corsFilter)
                //폼로그인과 기본적인 http 로그인 방식 사용안함
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or  hasRole('ROLE_MANAGER')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_ADMIN') or  hasRole('ROLE_MANAGER')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                //권한이 적은 사용자부터 권한이 많은 순으로
                .anyRequest().permitAll();
                //그외에는 모두 허용


        return http.build();
    }
}
