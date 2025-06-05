package studio.aroundhub.opensourceproject.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studio.aroundhub.opensourceproject.domain.user.UserDomain;
import studio.aroundhub.opensourceproject.dto.UserDto.LoginRequest;
import studio.aroundhub.opensourceproject.dto.UserDto.UserDto;
import studio.aroundhub.opensourceproject.service.UserService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/login")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입",description = "DB에 새로운 유저를 등록한다.")
    @PostMapping("/join")
    public ResponseEntity<String> signUp(@RequestBody UserDto user) {
        UserDomain findUser = userService.save(user);
        log.info("회원가입 요청");
        if (findUser != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 사용자입니다.");
    }

    @Operation(summary = "로그인", description = "로그인을 한다. 서버는 토큰와 Http 상태코드를 반환한다.")
    @PostMapping()
    public ResponseEntity<?>login(@RequestBody LoginRequest user)
    {
        log.info("로그인 요청");
        try{
           userService.login(user.getEmail(), user.getPassword());
           log.info("로그인 정상 처리 완료");
            return ResponseEntity.status(HttpStatus.OK).body("OK");
        } catch (RuntimeException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }





}
