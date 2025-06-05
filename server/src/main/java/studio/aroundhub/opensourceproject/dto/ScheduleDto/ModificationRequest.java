package studio.aroundhub.opensourceproject.dto.ScheduleDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModificationRequest {
    private String title;
    private String category;
    private LocalDate schedule;
}
