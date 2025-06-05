package studio.aroundhub.opensourceproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import studio.aroundhub.opensourceproject.dto.UserDto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void mockMvcSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }


    @DisplayName("회원가입 ")
    @Test
    void addUser() throws Exception {
        //given
        String url = "/login/join";
        String email = "tlgud119@naver.com";
        String password = "isee1214";
        String name = "이시형";

        UserDto dto = new UserDto(email,password,name);
        final String request = objectMapper.writeValueAsString(dto);

        ResultActions perform = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(request));
        perform.andExpect(status().isOk());


    }

    @Test
    void login() {




    }
}