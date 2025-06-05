package studio.aroundhub.opensourceproject.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import studio.aroundhub.opensourceproject.domain.schedule.ScheduleDomain;
import studio.aroundhub.opensourceproject.dto.ScheduleDto.ModificationRequest;
import studio.aroundhub.opensourceproject.dto.ScheduleDto.ScheduleRequest;
import studio.aroundhub.opensourceproject.repository.schedule.ScheduleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
      private final ScheduleRepository scheduleRepository;

    public ScheduleDomain save(ScheduleDomain scheduleDomain) {
        return scheduleRepository.save(scheduleDomain);
    }

    public ScheduleDomain Modification(Long id,String email, ModificationRequest modificationRequest)
    {
        ScheduleDomain findSchedule = scheduleRepository.findByIdAndEmail(id, email);
        if(findSchedule == null) {
            return null;
        }
        modification(findSchedule,modificationRequest);
        scheduleRepository.save(findSchedule);

        return findSchedule;
    }



    private void modification(ScheduleDomain findSchedule, ModificationRequest modificationRequest) {
        findSchedule.setTitle(modificationRequest.getTitle());
        findSchedule.setCategory(modificationRequest.getCategory());
        findSchedule.setSchedule(modificationRequest.getSchedule());

    }


    public boolean delete(Long id, String email) {
        ScheduleDomain findSchedule = scheduleRepository.findByIdAndEmail(id, email);
        if(findSchedule == null) {
            return false;
        }
        scheduleRepository.delete(findSchedule);
        return true;
    }

    public List<ScheduleRequest> getMonthSchedule(String email, int year, int month) {
        List<ScheduleDomain> domainList = scheduleRepository.findByEmailAndYearAndMonth(email, year, month);
        if(domainList == null) {
            System.out.println("null입니다");
            return null;
        }
        if(domainList.isEmpty()) {
            System.out.println("비어있습니다.");
            return new ArrayList<ScheduleRequest>();
        }
        List<ScheduleRequest> requestList = domainList.stream().map(schedule -> new ScheduleRequest(
               schedule.getId(),
                schedule.getTitle(),
                schedule.getCategory(),
                schedule.getSchedule()
        )).collect(Collectors.toList());
        return requestList;
    }

    //이메일을 기반으로 오늘 날자를 포함한 이후 데이터를 불러온다.
    public List<ScheduleRequest> getCategory(String email) {
        LocalDate today = LocalDate.now();
        List<ScheduleDomain> list = scheduleRepository.findByEmailAndScheduleGreaterThanEqual(email, today);
        if(list == null) {
            return null;
        }
        List<ScheduleRequest> requestList = list.stream().map(schedule -> new ScheduleRequest(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getCategory(),
                schedule.getSchedule()
        )).collect(Collectors.toList());
        return requestList;
    }
}
