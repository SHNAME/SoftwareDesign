package studio.aroundhub.opensourceproject.dto.TimetableDto;

import lombok.*;

import java.sql.Time;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddTimeTableDto {
    private String subject;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}