package studio.aroundhub.opensourceproject.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import studio.aroundhub.opensourceproject.domain.schedule.ScheduleDomain;
import studio.aroundhub.opensourceproject.dto.ScheduleDto.AddScheduleRequest;
import studio.aroundhub.opensourceproject.dto.ScheduleDto.ModificationRequest;
import studio.aroundhub.opensourceproject.dto.ScheduleDto.ScheduleRequest;
import studio.aroundhub.opensourceproject.service.ScheduleService;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
@Slf4j
public class ScheduleController {
    //일정 조회 화면
    private final ScheduleService scheduleService;

    @Operation(summary = "일정 추가", description = "eamil을 기반으로 일정을 추가한다.")
    @PostMapping()
    public ResponseEntity<String> newSchedule(@RequestBody AddScheduleRequest addScheduleRequest) {
        log.info("일정 추가 요청이 있음");
        if (addScheduleRequest == null ||
                addScheduleRequest.getTitle() == null ||
                addScheduleRequest.getCategory() == null ||
                addScheduleRequest.getSchedule() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("필수 필드가 누락되었습니다.");
        }
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ScheduleDomain newSchedule = ScheduleCreation(email, addScheduleRequest);
        ScheduleDomain scheduleDomain = scheduleService.save(newSchedule);
        if (scheduleDomain == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("OK");
    }




    @Operation(summary = "일정 수정", description = "요청한 일정을 Id와 email 기반으로 수정한다.")
    @PutMapping("/{id}")
    public ResponseEntity<String> ModificationSchedule
            (@PathVariable(value = "id") Long id, @RequestBody ModificationRequest modificationRequest )
    {
        log.info("일정 수정 요청을 받음 ");
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ScheduleDomain scheduleDomain = scheduleService.Modification(id,email,modificationRequest);
        if(scheduleDomain == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }


    @Operation(summary = "일정 삭제", description = "요청한 일정을 Id와 email 기반으로 삭제한다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteSchedule(@PathVariable(value = "id") Long id) {
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean deleted =  scheduleService.delete(id, email);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }


    @Operation(summary = "일정 조회",description = "요청한 년도와 달에 맞게 데이터를 불러온다")
    @GetMapping()
    public ResponseEntity<List<ScheduleRequest>> getMonthList(@RequestParam( name ="year") int year, @RequestParam(name ="month")  int month) {
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ScheduleRequest> list = scheduleService.getMonthSchedule(email,year,month);
        if(list == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(list.isEmpty()) { return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @Operation(summary = "카테고리 데이터 요청", description = "오늘을 포함한 사용자의 모든 데이터를 전송한다..")
    @GetMapping("/category")
    public ResponseEntity<List<ScheduleRequest>> getCategoryList() {
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info(email +"님의 카테고리 데이터 요청");
        List<ScheduleRequest> list = scheduleService.getCategory(email);
        if(list == null) {
            log.info("리스트","현재 null 상태");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if(list.isEmpty()) {
            log.info("리스트","리스트 데이터 없음");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
        }
        log.info("리스트 반환","${list.size()}");
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }



    private ScheduleDomain ScheduleCreation(String email, AddScheduleRequest addScheduleRequest) {
        if (addScheduleRequest == null) {
            throw new IllegalArgumentException("AddScheduleRequest 객체가 null입니다.");
        }
        ScheduleDomain scheduleDomain = ScheduleDomain.builder()
                .email(email)
                .title(addScheduleRequest.getTitle())
                .category(addScheduleRequest.getCategory())
                .schedule(addScheduleRequest.getSchedule())
                .build();

        return scheduleDomain;
    }


}
