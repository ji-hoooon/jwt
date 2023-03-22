package com.mino.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req= (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //사용자 인증 성공시 로그인 완료되면 Token : cos를 만들어서 응답시 토큰 전달
        //요청시마다 헤더의 Authorization에 value값으로 토큰을 전달
        //토큰이 전달되면 서버가 생성한 토큰이 맞는지 검증한다. : RSA/ HS256으로 검증

        //POST 요청 && 서버 키값이 일치할때만 필터
        if(req.getMethod().equals("POST")){
            System.out.println("POST 요청됨");
            String headerAuth = req.getHeader("Authorization");
            System.out.println("headerAuth = " + headerAuth);
            System.out.println("필터3");

            if(headerAuth.equals("cos")){
                chain.doFilter(request,response);
            }else {
                PrintWriter out=res.getWriter();
                out.println("인증 실패");
            }
        }
    }
}
