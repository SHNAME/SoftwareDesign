package studio.aroundhub.opensourceproject.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import studio.aroundhub.opensourceproject.domain.schedule.ScheduleDomain;
import studio.aroundhub.opensourceproject.dto.ScheduleDto.AddScheduleRequest;
import studio.aroundhub.opensourceproject.dto.ScheduleDto.ModificationRequest;
import studio.aroundhub.opensourceproject.service.ScheduleService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;
    //서버를 실행하지 않고 가짜 api 요청을 해서 응답을 받아 볼 수 있다.
    //실제 서버를 동작하지 않고 검증이 가능하다.

    @Autowired
    private ScheduleService scheduleService;



    @Autowired
    private ObjectMapper objectMapper;// 직렬화와 역직렬화에 사용된다.


    // 테스트: 일정 추가
    @Test
    @WithMockUser(username = "tlgud119@naver.com") // 인증된 사용자 추가
    void testNewSchedule() throws Exception {
        LocalDate date = LocalDate.now();
        AddScheduleRequest request = new AddScheduleRequest();

        request.setTitle("Meeting");
        request.setCategory("Work");
        request.setSchedule(date);
        mockMvc.perform(post("/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                      //  .with(csrf())  // CSRF 토큰 추가
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("OK"));
    }



    @Test
    @WithMockUser(username = "tlgud119@naver.com", roles = "USER")
    void testModificationSchedule() throws Exception {
        LocalDate date = LocalDate.now();
        ScheduleDomain save = scheduleService.save(
                ScheduleDomain.builder().email("tlgud119@naver.com").title("title")
                        .category("category").schedule(date).build());
        if(save == null) {
            throw new IllegalArgumentException("저장에 실패했습니다.");
        }
        ModificationRequest request =
                new ModificationRequest("newtitle",
                        "newcategory",
                        date,
                        true);
        int id =(int)save.getId();
        mockMvc.perform(put("/schedule/" +id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));

    }


    // 테스트: 일정 삭제
    @Test
    @WithMockUser(username = "tlgud119@naver.com", roles = "USER")
    void testDeleteSchedule() throws Exception {
        LocalDate date = LocalDate.now();
        ScheduleDomain save = scheduleService.save(
                ScheduleDomain.builder().email("tlgud119@naver.com").title("title")
                        .category("category").schedule(date).build());
        if(save == null) {
            throw new IllegalArgumentException("저장에 실패했습니다.");
        }
        int id =(int)save.getId();
        mockMvc.perform(delete("/schedule/" +id))
                .andExpect(status().isNoContent())
                .andExpect(content().string("true"));
    }




    // 테스트: 특정 달의 일정 조회
    @Test
    @WithMockUser(username = "tlgud119@naver.com", roles = "USER")
    void testGetMonthList() throws Exception {
        LocalDate date = LocalDate.now();
        ScheduleDomain save = scheduleService.save(
                ScheduleDomain.builder().email("tlgud119@naver.com").title("title")
                        .category("category").schedule(date).build());
        if(save == null) {
            throw new IllegalArgumentException("저장에 실패했습니다.");
        }
        mockMvc.perform(get("/schedule")
                        .param("year", "2025")
                        .param("month", "1"))
                .andExpect(status().isOk());
    }

    // 테스트: 카테고리별 일정 조회
    @Test
    @WithMockUser(username = "tlgud119@naver.com", roles = "USER")
    void testGetCategoryList() throws Exception {
        LocalDate date = LocalDate.now();
        ScheduleDomain save = scheduleService.save(
                ScheduleDomain.builder().email("tlgud119@naver.com").title("title")
                        .category("category").schedule(date).build());
        if(save == null) {
            throw new IllegalArgumentException("저장에 실패했습니다.");
        }

        mockMvc.perform(get("/schedule/category"))
                .andExpect(status().isOk());
    }




}
