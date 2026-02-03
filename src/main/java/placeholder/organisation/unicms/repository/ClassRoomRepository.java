package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.ClassRoom;

import java.util.Optional;
import java.util.function.Consumer;

@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
    Optional<ClassRoom> findByRoom(String name);

    Consumer<? super ClassRoom> delete(Optional<ClassRoom> classRoom);
}
