package com.mino.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mino.jwt.config.auth.PrincipalDetails;
import com.mino.jwt.model.User;
import com.mino.jwt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//시큐리티의 자동구성 필터 중, BasicAuthenticationFilter
//: 권한이나 인증이 필요한 특정 주소를 요청했을 때 BasicAuthenticationFilter 필터링 수행
//만약 권한이 인증이 필요한 주소가 아닌 경우에는 필터링을 수행하지 않는다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    //생성자 의존성 주입
    private  UserRepository userRepository;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {

        super(authenticationManager);
        this.userRepository=userRepository;
    }

    //인증이나 권한이 필요한 주소 요청이 있을 때 해당 필터를 타게 될 것
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        super.doFilterInternal(request, response, chain);
        System.out.println("인증이나 권한이 필요한 주소 요청");

        //1. 헤더 값 확인
        String jwtHeader = request.getHeader("Authorization");
        System.out.println("jwtHeader = " + jwtHeader);

        //3. 받은 헤더 값은 JWT 토큰인데 토큰 검증을 해 잘못된 토큰 거르기
        if(jwtHeader==null || !jwtHeader.startsWith("Bearer")){
            chain.doFilter(request,response);
            return;
        }

        //3. 토큰 검증을 해 정상적인 사용자인지 확인
        String jwtToken = request.getHeader("Authorization").replace("Bearer ","");
        //4. 정상적인 사용자면
        // 알고리즘을 통해 서버 키값인지 확인하고, 키값이 정상적이면, 토큰의 클레임(데이터)에서 사용자이름을 가져온다.
        String username = JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username").asString();

        //5. 유저이름을 보고, 서명여부 판단
        if(username!=null){
            //6. 시큐리티 설정 클래스에서 리포지토리 연결 -> 생성자 의존성 주입
            //: 연결 해서, DB 조회
            User userEntity = userRepository.findByUsername(username);
            //7. 정상 확인 완료시 PrincipalDetails 생성
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            //8. 정상 확인 완료시 강제로 Authentication 생성 -> 로그인 토큰 생성해서 강제 생성
            //: jwt토큰 서명을 통해서 서명이 정상이면, 사용자 정보 전달, 인증정보는 Null, 권한 정보 전달해서 Authentication 객체 생성
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            //9. 강제로 시큐리티 세션에 접근해 Authentication 객체를 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request,response);
        }
    }
}
