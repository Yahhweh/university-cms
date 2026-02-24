package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.entity.Lesson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson> {

    List<Lesson> findLessonsByLecturerId(Long lecturerId);

    @Query("SELECT l FROM Lesson l " +
            "JOIN Student s ON s.id = :personId " +
            "WHERE l.date = :date AND l.group.id = s.group.id")
    List<Lesson> findByDateForStudent(
            @Param("date") LocalDate date,
            @Param("personId") Long personId
    );

    List<Lesson> findByDateAndLecturerId(LocalDate date, Long lecturerId);

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

    @Query("SELECT COUNT(l) > 0 FROM Lesson l " +
            "JOIN l.duration d " +
            "WHERE l.lecturer.id = :lecturer_id " +
            "AND l.date = :date " +
            "AND d.end > :start " +
            "AND d.start < :end " +
            "AND (:excludeId = -1 OR l.id <> :excludeId)")
    boolean findConflictionLessonsForLecturer(
            @Param("lecturer_id") Long lecturerId,
            @Param("date") LocalDate date,
            @Param("start") LocalTime startTime,
            @Param("end") LocalTime endTime,
            @Param("excludeId") Long excludeId);

        @Query("SELECT COUNT(l) > 0 FROM Lesson l " +
                "WHERE l.room.id = :id " +
                "AND l.date = :date " +
                "AND l.duration.start < :end " +
                "AND l.duration.end > :start " +
                "AND (:excludeId IS NULL OR l.id <> :excludeId)")
        boolean findRoomConflictsInTime(
                @Param("date") LocalDate date,
                @Param("start") LocalTime start,
                @Param("end") LocalTime end,
                @Param("id") Long id,
                @Param("excludeId") Long excludeId);

    @Query("SELECT COUNT(l) > 0 FROM Lesson l " +
            "WHERE l.group.id = :groupId " +
            "AND l.date = :date " +
            "AND l.duration.start < :end " +
            "AND l.duration.end > :start " +
            "AND (:excludeId IS NULL OR l.id <> :excludeId)")
    boolean findGroupConflictInTime(
            @Param("groupId") Long groupId,
            @Param("date") LocalDate date,
            @Param("start") LocalTime start,
            @Param("end") LocalTime end,
            @Param("excludeId") Long excludeId);
    }