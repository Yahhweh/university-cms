package placeholder.organisation.unicms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.ClassRoom;

import java.util.Optional;

@Repository
public interface ClassRoomDao extends JpaRepository<ClassRoom, Long> {
 Optional<ClassRoom> findByRoom(String name);
 }
