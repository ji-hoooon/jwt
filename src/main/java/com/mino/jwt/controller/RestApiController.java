package com.mino.jwt.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

//인증 요청은 불가능하기 때문에 직접 설명
//@CrossOrigin
@RestController
public class RestApiController {
    @GetMapping("home")
    public String home() {
        return "<h1>home</h1>";
    }
    @PostMapping("token")
    public String token() {
        return "<h1>token</h1>";
    }
}
