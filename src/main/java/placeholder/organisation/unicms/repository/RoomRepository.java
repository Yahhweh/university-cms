package placeholder.organisation.unicms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Room;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByRoom(String name);

}
