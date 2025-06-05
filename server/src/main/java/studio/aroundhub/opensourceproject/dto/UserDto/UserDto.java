package studio.aroundhub.opensourceproject.dto.UserDto;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import studio.aroundhub.opensourceproject.domain.user.UserDomain;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String email;
    private String password;
    private String name;

    //비밀번호 해쉬를 위해서 의존성 주입
    public UserDomain toEntity(PasswordEncoder passwordEncoder)
    {
        return UserDomain.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .build();
    }



}
