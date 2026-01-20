package placeholder.organisation.unicms.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import placeholder.organisation.unicms.entity.ClassRoomType;

import java.util.Optional;

@Repository
public interface ClassRoomTypeJpa extends JpaRepository<ClassRoomType, Long> {
    Optional<ClassRoomType> findByName(String name);
}
