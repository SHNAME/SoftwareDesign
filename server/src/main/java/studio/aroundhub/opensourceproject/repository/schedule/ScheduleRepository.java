package studio.aroundhub.opensourceproject.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import studio.aroundhub.opensourceproject.domain.schedule.ScheduleDomain;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleDomain, Long> {

    ScheduleDomain findByIdAndEmail(Long id, String email); // ID와 이메일로 찾기

    @Query("SELECT s FROM ScheduleDomain s WHERE s.email = :email AND YEAR(s.schedule) = :year AND MONTH(s.schedule) = :month")
    List<ScheduleDomain> findByEmailAndYearAndMonth(@Param("email") String email, @Param("year") int year, @Param("month") int month);

    // 이메일을 기반으로 오늘 날짜를 포함한 이후 데이터를 불러온다.
    List<ScheduleDomain> findByEmailAndScheduleGreaterThanEqual(String email, LocalDate today);
}

