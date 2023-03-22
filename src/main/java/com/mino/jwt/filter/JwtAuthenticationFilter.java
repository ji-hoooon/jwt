package com.mino.jwt.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mino.jwt.config.auth.PrincipalDetails;
import com.mino.jwt.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.Buffer;


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
        //1. 사용자 정보를 받는다.
        try {
//            //일반적인 파싱 방법
//            //1. 버퍼드리더 만들기
//            BufferedReader br = request.getReader();
//            //2. input을 선언 후, null이 될때까지 반복
//            String input=null;
//            while((input=br.readLine())!=null){
//                System.out.println("input = " + input);
//            }
//            //username=ssar&password=1234
//
//            System.out.println("request.getInputStream() = " + request.getInputStream());
//            //username, password가 담겨져 있다.

            //JSON 파싱 방법
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println("user = " + user);
            //2. 로그인 시도 - authenticationManager로 로그인 시도해서 PrincipalDetailService 호출
            //:formLogin()을 사용하지 않기 때문에 직접 구성해야한다.
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            //: PrincipalDetailService가 loadUserByUsername() 호출 -> password는 내부적으로 인증 처리 수행
            //-> 정상인 경우 리턴된다.
            //: 리턴된 authentication에 사용자 정보가 담겨져 있다.
            //-> DB에 있는 사용자정보와 일치한다.

            //3. PrincipalDetails를 세션에 담는다.
            //: 스프링 시큐리티는 세션에 PrincipalDetails가 있어야 권한관리 가능
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            //: Object -> PrincipalDetails
            //-> 로그인이 정상 완료 -> 인증 완료

            System.out.println("로그인 완료 : principalDetails.getUser() = " + principalDetails.getUser());

            //4. JWT 토큰을 생성해 응답 -> susccessfulAuthentication에서 생성해 응답

            return authentication;
            //authentication 객체가 리턴될 때 session 영역에 저장 -> 로그인 되었다는 의미
            //-> 권한 관리를 편하게 시큐리티로 하기 위해서

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //attemptAuthentication 인증 완료시 susccessfulAuthentication 실행
    //attemptAuthentication 인증 실패시 unsusccessfulAuthentication 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //4. JWT 토큰을 생성해 응답 -> JWT 토큰을 request 요청한 사용자에게 응답
        System.out.println("susccessfulAuthentication 실행됨");
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
