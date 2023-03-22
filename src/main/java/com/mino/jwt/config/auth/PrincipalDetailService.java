package com.mino.jwt.config.auth;

import com.mino.jwt.model.User;
import com.mino.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//로그인 요청시 동작하는 서비스인데 /login이 formLogin.disable()로 동작하지않음
//: 직접 PrincipalDetailService을 호출하는 필터를 작성해야한다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailService의 loadUserByUsername");
        User userEntity=userRepository.findByUsername(username);
        return new PrincipalDetails(userEntity);
    }
}
