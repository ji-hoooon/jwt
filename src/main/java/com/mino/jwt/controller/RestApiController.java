package com.mino.jwt.controller;

import com.mino.jwt.model.User;
import com.mino.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

//인증 요청은 불가능하기 때문에 직접 설명
//@CrossOrigin
@RestController
@RequiredArgsConstructor
public class RestApiController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("home")
    public String home() {
        return "<h1>home</h1>";
    }
    @PostMapping("token")
    public String token() {
        return "<h1>token</h1>";
    }

    @PostMapping("join")
    public String join(@RequestBody User user){
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "회원가입완료";
    }
}
