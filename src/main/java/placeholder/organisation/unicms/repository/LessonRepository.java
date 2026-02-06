package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.entity.PersonType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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

    default List<Lesson> findByDateAndRole(LocalDate date, Long personId, PersonType role) {
        if (role == PersonType.Student) {
            return findByDateForStudent(date, personId);
        } else {
            return findByDateForLecturer(date, personId);
        }
    }

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
            "WHERE l.lecturer.id = :lectuerer_id")
    List<Lesson> findAllLessonsRelatedToLecturer(@Param("lecturer_id") Long lecturerId);

    @Query("SELECT cr FROM ClassRoom cr WHERE cr.id NOT IN " +
            "(SELECT l.classRoom.id FROM Lesson l " +
            "WHERE l.date = :date " +
            "AND l.duration.start < :endTime " +
            "AND l.duration.end > :startTime)")
    List<ClassRoom> findFreeClassRooms(@Param("date") LocalDate date,
                                       @Param("startTime") LocalTime startTime,
                                       @Param("endTime") LocalTime endTime);
    default List<Lesson> findInRange(LocalDate fromDate, LocalDate toDate, Long personId, PersonType role) {
        if (role == PersonType.Student) {
            return findInRangeForStudent(fromDate, toDate, personId);
        } else {
            return findInRangeForLecturer(fromDate, toDate, personId);
        }
    }

}