package com.mino.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mino.jwt.config.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//스프링시큐리티에 기본등록되는 UsernamePasswordAuthenticationFilter를 상속해서 사용
//login 요청시 post 메서드 전송시 동작한다.
//: 스프링시큐리티에 다시 등록한다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    //authenticationManager를 받아서 login 요청시 로그인을 위해 실행하는 함수
    //1. 사용자 정보를 받는다.
    //2. 로그인 시도 - authenticationManager로 로그인 시도해서 PrincipalDetailService 호출
    //: PrincipalDetailService가 loadUserByUsername() 호출
    //3. PrincipalDetails를 세션에 담는다.
    //: 스프링 시큐리티는 세션에 PrincipalDetails가 있어야 권한관리 가능
    //4. JWT 토큰을 생성해 응답
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("로그인 요청시 JwtAuthenticationFilter의 attemptAuthentication 로그인 동작");
        return super.attemptAuthentication(request, response);
    }
}
