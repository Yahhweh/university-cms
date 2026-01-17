package placeholder.organisation.unicms.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.Address;
import placeholder.organisation.unicms.entity.ClassRoom;

@Repository
public interface ClassRoomJpa extends JpaRepository<ClassRoom, Long> {
}
