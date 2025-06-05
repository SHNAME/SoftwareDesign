package studio.aroundhub.opensourceproject.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import studio.aroundhub.opensourceproject.domain.user.UserDomain;
import studio.aroundhub.opensourceproject.dto.UserDto.UserDto;
import studio.aroundhub.opensourceproject.repository.user.UserRepository;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletResponse response;

    @Value("${jwt.signing.key}")
    private  String signingKey;


    //신규 회원 등록
    public UserDomain save(UserDto user) {
        UserDomain findEmail = userRepository.findByEmail(user.getEmail());
        if (findEmail != null) {
            return null; //이미 email이 있는 경우는 저장할 수 없음
        }
        UserDomain entity = user.toEntity(passwordEncoder);
        return userRepository.save(entity);
    }

    //로그인 기능(성공하면 토큰을 반환)
    public void login(String email, String password) {
        UserDomain findUser = userRepository.findByEmail(email);
        if(findUser == null)
        {
           throw  new RuntimeException("이메일을 다시 입력해주세요");
        }
        if(!passwordEncoder.matches(password, findUser.getPassword())){
            throw  new BadCredentialsException("비밀번호를 다시 입력해주세요.");
        }
        //이메일도 일치하고 비밀번호도 다 일치하는 경우
        SecretKey key = Keys.hmacShaKeyFor(
                signingKey.getBytes(StandardCharsets.UTF_8)
        );
        String jwt = Jwts.builder()
                .setClaims(Map.of("email",findUser.getEmail()))
                .signWith(key)
                .compact();

        response.setHeader("Authorization", jwt);
    }



}
