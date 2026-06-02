package com.tenco.csr_blog_v1.core.util;

import com.tenco.csr_blog_v1.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// JwtUtil 클래스를 테스트 코드 실행시 IoC 처리 (스프링 컨테이너에 강제로 등록해서 테스트 환경에 사용할 수 있게 함)
@Import({JwtUtil.class})
@DataJpaTest // DB(JPA) 관련된 설정만 빠르게 로드하여 테스트하는 어노테이션 이다.
public class JwtUtilTest {


    // 샘플 데이터 설정
    User user = User.builder()
            .id(1)
            .username("ssar")
            .roles(List.of("USER"))
            .build();

    // @Test 가 붙은 메서드는 톰캣 서버를 켜지 않아도 뒤에 숨어있는 테스트 엔진이 알아서 실행 시킬 수 있도록 처리 한다
    @Test
    public void jwtUtils_test() {

        // Given : 테스트를 하기 위한 상태를 코드로 작성합니다.
        String bearerJwt = JwtUtil.create(user);

        // When : 우리가 진짜로 검증하고 싶은 핵심 동작을 코드로 작성합니다.
        String jwt = bearerJwt.substring(JwtUtil.TOKEN_PREFIX.length());
        // jwt <-- 순수 토큰만 저장 됨.

        // 순수 토큰을 디코딩
        User decodedToken = JwtUtil.verify(jwt);

        // 임시 :  눈으로 보는 코드
//        System.out.println("bearerJwt : " + bearerJwt);
//        System.out.println("순수 JWT : " + jwt);
//        System.out.println("토큰에서 추출한 user id : " + decodedToken.getId());
//        System.out.println("토큰에서 추출한 username : " + decodedToken.getUsername());
//        System.out.println("토큰에서 추출한 rolse : " + decodedToken.getRoles());


        // Then : 검증하는 부분을 코드로 작성합니다.
        // AssertJ 라이브러리 사용
        assertThat(decodedToken.getId()).isEqualTo(1);
        assertThat(decodedToken.getUsername()).isEqualTo("ssar");
        assertThat(decodedToken.getRoles()).containsExactly("USER");
        //assertThat(decodedToken.getRoles()).isEqualTo("USER");

    }

}


