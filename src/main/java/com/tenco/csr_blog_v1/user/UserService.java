package com.tenco.csr_blog_v1.user;

import com.tenco.csr_blog_v1.auth.AuthRequest;
import com.tenco.csr_blog_v1.auth.AuthResponse;
import com.tenco.csr_blog_v1.core.handler.errors.BadRequestException;
import com.tenco.csr_blog_v1.core.handler.errors.NotFoundException;
import com.tenco.csr_blog_v1.core.handler.errors.UnauthorizedException;
import com.tenco.csr_blog_v1.core.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public AuthResponse.DTO 회원가입(AuthRequest.JoinDTO requestDTO) {
        if(userRepository.findByUsername(requestDTO.username()).isPresent()) {
            throw  new BadRequestException("이미 존재하는 유저네임입니다");
        }
        String encPassword = bCryptPasswordEncoder.encode(requestDTO.password());
        User savedUser = userRepository.save(requestDTO.toEntity(encPassword));
        return new AuthResponse.DTO(savedUser);
    }


    public String 로그인(AuthRequest.LoginDTO requestDTO) {
        User findUser = userRepository.findByUsername(requestDTO.username())
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));

        if(bCryptPasswordEncoder.matches(requestDTO.password(), findUser.getPassword()) == false) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다");
        }
        // 토큰 자동 생성
        return JwtUtil.create(findUser);
    }

    public AuthResponse.AvailableDTO 유저네임중복체크(String username) {
       boolean isAvailable = !userRepository.existsByUsername(username);
       return new AuthResponse.AvailableDTO(isAvailable);
    }

}
