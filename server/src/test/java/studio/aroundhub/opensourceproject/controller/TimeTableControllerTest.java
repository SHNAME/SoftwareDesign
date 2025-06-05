package studio.aroundhub.opensourceproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import studio.aroundhub.opensourceproject.domain.time.TimeDomain;
import studio.aroundhub.opensourceproject.dto.TimetableDto.AddTimeTableDto;
import studio.aroundhub.opensourceproject.repository.time.TimeTableRepository;

import java.sql.Time;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TimeTableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TimeTableController timeTableController;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TimeTableRepository timeTableRepository;

    @Test
    @DisplayName("과목 추가 - 성공 케이스")
    @WithMockUser(username = "tlgud119@naver.com", roles = "USER")
    void testAddTimetable_Success() throws Exception {
        // Given: 테스트 요청 데이터 생성
        AddTimeTableDto requestDto = new AddTimeTableDto("Math", "MON,WED",
                Time.valueOf("09:00" + ":00"), Time.valueOf("12:00" + ":00"));


        mockMvc.perform(MockMvcRequestBuilders.post("/timetable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("OK"));
    }




    @Test
    @DisplayName("과목 추가 - 실패 케이스 (필수 필드 누락)")
    @WithMockUser(username = "tlgud119@naver.com", roles = "USER")
    void testAddTimetable_MissingField() throws Exception {
        // Given: 요청 데이터가 비어 있음
       AddTimeTableDto requestDto = new AddTimeTableDto();

        // When & Then: 엔드포인트 호출 및 검증
        mockMvc.perform(MockMvcRequestBuilders.post("/timetable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest()) // 400 Bad Request 상태 코드
                .andExpect(content().string("필수 필드가 누락되었습니다."));
    }

    @Test
    @DisplayName("과목 조회 - 데이터 있음")
    @WithMockUser(username = "tlgud119@naver.com", roles = "USER")
    void testGetTimeTable_Success() throws Exception {
        //when
        AddTimeTableDto requestDto = new AddTimeTableDto("Math", "MON,WED",
                Time.valueOf("09:00" + ":00"), Time.valueOf("12:00" + ":00"));

        AddTimeTableDto requestDto2 = new AddTimeTableDto("soccer", "WED,FRI",
                Time.valueOf("09:00" + ":00"), Time.valueOf("12:00" + ":00"));


        //then
        timeTableController.addTimetable(requestDto);
        timeTableController.addTimetable(requestDto2);


        mockMvc.perform(get("/timetable"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }


    @Test
    @DisplayName("과목 조회 - 데이터 없음")
    @WithMockUser(username = "tlgud119@naver.com", roles = "USER")
    void testGetTimeTable_NoContent() throws Exception {

        mockMvc.perform(get("/timetable"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("과목 수정 - 성공 케이스")
    @WithMockUser(username = "tlgud119@naver.com", roles = "USER")
    void testCorrectionTimeTable_Success() throws Exception {

        AddTimeTableDto requestDto = new AddTimeTableDto("Math", "MON,WED",
                Time.valueOf("09:00" + ":00"), Time.valueOf("12:00" + ":00"));
        timeTableController.addTimetable(requestDto);

        List<TimeDomain> byEmail = timeTableRepository.findByEmail("tlgud119@naver.com");
        TimeDomain timeDomain = byEmail.get(0);


        AddTimeTableDto newDto = new AddTimeTableDto(
                "Updated Subject", "FRI",
                Time.valueOf("14:00:00"), Time.valueOf("16:00:00"));


        mockMvc.perform(MockMvcRequestBuilders.put("/timetable/" + timeDomain.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    @DisplayName("과목 삭제 - 성공 케이스")
    @WithMockUser(username = "tlgud119@naver.com", roles = "USER")
    void testDeleteTimeTable_Success() throws Exception {
        AddTimeTableDto requestDto = new AddTimeTableDto("Math", "MON,WED",
                Time.valueOf("09:00" + ":00"), Time.valueOf("12:00" + ":00"));
        timeTableController.addTimetable(requestDto);

        List<TimeDomain> byEmail = timeTableRepository.findByEmail("tlgud119@naver.com");
        TimeDomain timeDomain = byEmail.get(0);


        mockMvc.perform(MockMvcRequestBuilders.delete("/timetable/" + timeDomain.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }


    @Test
    @DisplayName("과목 삭제 - 실패 케이스")
    @WithMockUser(username = "tlgud119@naver.com", roles = "USER")
    void testDeleteTimeTable_Failure() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/timetable/0"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("false"));
    }




}
