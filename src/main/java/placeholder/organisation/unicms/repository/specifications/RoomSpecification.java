package placeholder.organisation.unicms.repository.specifications;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.service.dto.request.filter.RoomFilter;

import java.util.ArrayList;
import java.util.List;

public class RoomSpecification {
    public static Specification<Room> filter(RoomFilter requestDTO){
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(requestDTO.getRoomType() != null && !requestDTO.getRoomType().isBlank()){
                predicates.add(cb.like(cb.lower(root.get("roomType").get("name")), "%" + requestDTO.getRoomType().toLowerCase() + "%"));
            }
            if(requestDTO.getNumber() != null && !requestDTO.getNumber().isBlank()){
                predicates.add(cb.like(cb.lower(root.get("room")),  "%" + requestDTO.getNumber().toLowerCase() + "%" ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
