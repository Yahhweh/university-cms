package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.DayOfWeek;
import placeholder.organisation.unicms.entity.Schedule;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {

    List<Schedule> findByGroup_Id(Long groupId);

    @Query("SELECT COUNT(s) > 0 FROM Schedule s " +
        "WHERE s.lecturer.id = :lecturerId AND s.day = :day " +
        "AND s.duration.id = :durationId AND s.id <> :excludeId")
    boolean existsLecturerConflict(@Param("lecturerId") Long lecturerId,
                                   @Param("day") DayOfWeek day,
                                   @Param("durationId") Long durationId,
                                   @Param("excludeId") Long excludeId);
}
