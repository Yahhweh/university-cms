package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.ClassRoom;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    Optional<ClassRoom> findByRoom(String name);

    @Query("SELECT cr FROM ClassRoom cr WHERE cr.id NOT IN " +
            "(SELECT l.classRoom.id FROM Lesson l " +
            "WHERE l.date = :date " +
            "AND l.duration.start < :endTime " +
            "AND l.duration.end > :startTime)")
    List<ClassRoom> findFreeClassRooms(@Param("date") LocalDate date,
                                       @Param("startTime") LocalTime startTime,
                                       @Param("endTime") LocalTime endTime);

}
