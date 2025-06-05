package studio.aroundhub.opensourceproject.dto.ScheduleDto;

import lombok.*;

import java.time.LocalDate;

//일정 요청을 위한 DTO

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ScheduleRequest {
    Long id;
    String title;
    String category;
    LocalDate scheduleDate;

}
