package studio.aroundhub.opensourceproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studio.aroundhub.opensourceproject.domain.time.TimeDomain;
import studio.aroundhub.opensourceproject.dto.TimetableDto.AddTimeTableDto;
import studio.aroundhub.opensourceproject.repository.time.TimeTableRepository;

import java.sql.Time;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeTableService {
    private final TimeTableRepository timeTableRepository;

    public TimeDomain save(TimeDomain newTimeDomain) {
        if(newTimeDomain == null)
        {
            return null;
        }
        return timeTableRepository.save(newTimeDomain);
    }

    public List<TimeDomain> getTimeTable(String email) {
        //이메일을 기반으로 시간표 데이터를 전체를 다 불러온다.
        return timeTableRepository.findByEmail(email);
    }

    public TimeDomain correction(String email, long id, AddTimeTableDto addTimeTableDto) {
        TimeDomain findDomain = timeTableRepository.findByEmailAndId(email, id);
        correctionDomain(findDomain, addTimeTableDto);
        timeTableRepository.save(findDomain);
        return findDomain;
    }

    public Time convertToSqlTime(String timeString) {
        return Time.valueOf(timeString);
    }

    private TimeDomain correctionDomain(TimeDomain timeDomain, AddTimeTableDto addTimeTableDto) {
        timeDomain.setSubjectName(addTimeTableDto.getSubject());
        timeDomain.setDayOfWeek(addTimeTableDto.getDayOfWeek());
        timeDomain.setStartTime(convertToSqlTime(addTimeTableDto.getStartTime()));
        timeDomain.setEndTime(convertToSqlTime(addTimeTableDto.getEndTime()));
        return timeDomain;
    }

    public boolean deleteTimeTable(long id, String email) {
        TimeDomain byEmailAndId = timeTableRepository.findByEmailAndId(email, id);
        if(byEmailAndId == null)
        {
            return false;
        }
        timeTableRepository.delete(byEmailAndId);
        return true;
    }

    public boolean deleteAll(String email) {
        List<TimeDomain> timeDomains = timeTableRepository.findByEmail(email);
        if(timeDomains.isEmpty())
        {
            return false;
        }
        timeTableRepository.deleteAll(timeDomains);
        return true;
    }
}
