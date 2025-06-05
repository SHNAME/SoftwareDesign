package studio.aroundhub.opensourceproject.dto.UserDto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String email;
    private String password;
}
