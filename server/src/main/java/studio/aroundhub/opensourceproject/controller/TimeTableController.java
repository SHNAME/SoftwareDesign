package studio.aroundhub.opensourceproject.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import studio.aroundhub.opensourceproject.domain.time.TimeDomain;
import studio.aroundhub.opensourceproject.dto.TimetableDto.AddTimeTableDto;
import studio.aroundhub.opensourceproject.service.TimeTableService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timetable")
@Slf4j
public class TimeTableController {
    private final TimeTableService timeTableService;

    @Operation(summary = "과목 추가",description = "과목을 추가합니다.")
    @PostMapping()
    public ResponseEntity<Void> addTimetable(@RequestBody AddTimeTableDto request) {
        log.info("과목 추가 요청");

        if (addDataVerification(request)) {
            log.info("필수 필드 누락");
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TimeDomain newTimeDomain = timeDomainCreation(email, request);
        TimeDomain result = timeTableService.save(newTimeDomain);

        if (result == null) {
            log.info("과목 추가 실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        }

        log.info("과목 추가 성공");
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
    }

    @Operation(summary = "과목 조회", description = "email을 기반으로 시간표 데이터를 모두 불러온다")
    @GetMapping()
    public ResponseEntity<List<TimeDomain>>getTimeTable(){
        log.info("과목조회 요청");
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<TimeDomain> list = timeTableService.getTimeTable(email);
        if(list == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        if(list.isEmpty())
        {
            log.info("과목이 없습니다.");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        log.info("과목 조회 성공 " + list.size());
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @Operation(summary = "시간표 수정", description = "email과 id를 기반으로 시간표 데이터를 수정한다")
    @PutMapping("/{id}")
    public ResponseEntity<Void> modificationTimeTable(@PathVariable("id") long id, @RequestBody AddTimeTableDto request){
        log.info("시간표 수정 요청");
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TimeDomain correctionTable = timeTableService.correction(email,id,request);
        if(correctionTable == null)
        {
            log.info("시간표 수정 요청 실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        log.info("시간표 수정 요청 성공");
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "시간표 삭제", description = "email과 id를 기반으로 시간표 데이터를 삭제한다")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean>deleteTimeTable(@PathVariable("id") long id){
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean deletedTable = timeTableService.deleteTimeTable(id,email);
        if(deletedTable == false)//삭제하지 못했다면
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @Operation(summary = "시간표 전체 삭제", description = "email과 id를 기반으로 시간표 데이터를  전체 삭제한다")
    @DeleteMapping()
    public ResponseEntity<Boolean>deleteAllTimeTable(){
        String email = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean deletedTable = timeTableService.deleteAll(email);
        if(deletedTable == false)//삭제하지 못했다면
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }






    private  boolean addDataVerification(AddTimeTableDto request) {
        return request == null || request.getSubject() == null ||
                request.getStartTime() == null || request.getEndTime() == null;
    }


    private TimeDomain timeDomainCreation(String email, AddTimeTableDto request) {
        return TimeDomain.builder().email(email).subjectName(request.getSubject())
                .dayOfWeek(request.getDayOfWeek()).startTime(timeTableService.convertToSqlTime(request.getStartTime()))
                .endTime(timeTableService.convertToSqlTime(request.getEndTime())).build();
    }







}
