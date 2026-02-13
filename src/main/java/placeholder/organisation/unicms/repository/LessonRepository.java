package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Lesson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findByLecturerId(Long lecturerId);

    @Query("SELECT l FROM Lesson l " +
            "JOIN Student s ON s.id = :personId " +
            "WHERE l.date = :date AND l.group.id = s.group.id")
    List<Lesson> findByDateForStudent(
            @Param("date") LocalDate date,
            @Param("personId") Long personId
    );

    @Query("SELECT l FROM Lesson l " +
            "WHERE l.date = :date AND l.lecturer.id = :personId")
    List<Lesson> findByDateForLecturer(
            @Param("date") LocalDate date,
            @Param("personId") Long personId
    );

    @Query("SELECT l FROM Lesson l " +
            "JOIN Student s ON s.id = :personId " +
            "WHERE l.date BETWEEN :fromDate AND :toDate " +
            "AND l.group.id = s.group.id " +
            "ORDER BY l.date ASC")
    List<Lesson> findInRangeForStudent(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("personId") Long personId
    );

    @Query("SELECT l FROM Lesson l " +
            "WHERE l.date BETWEEN :fromDate AND :toDate " +
            "AND l.lecturer.id = :personId " +
            "ORDER BY l.date ASC")
    List<Lesson> findInRangeForLecturer(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("personId") Long personId
    );

    @Query("SELECT l FROM Lesson l " +
            "WHERE l.lecturer.id = :lecturer_id " +
            "AND l.date = :date " +
            "AND l.duration.end > :start " +
            "AND l.duration.start < :end")
    List<Lesson> findConflictionLessonsForLecturer(@Param("lecturer_id") Long lecturerId,
                                                 @Param("date") LocalDate date,
                                                 @Param("start") LocalTime startTime,
                                                 @Param("end") LocalTime endTime);

}