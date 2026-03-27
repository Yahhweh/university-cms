package placeholder.organisation.unicms.repository.specifications;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import placeholder.organisation.unicms.entity.User;
import placeholder.organisation.unicms.service.dto.request.filter.UserFilterRequestDTO;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> filter(UserFilterRequestDTO userFilterRequestDTO) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userFilterRequestDTO.getName() != null && !userFilterRequestDTO.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + userFilterRequestDTO.getName().toLowerCase() + "%"));
            }
            if (userFilterRequestDTO.getSureName() != null && !userFilterRequestDTO.getSureName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("sureName")), "%" + userFilterRequestDTO.getSureName().toLowerCase() + "%"));
            }
            if (userFilterRequestDTO.getEmail() != null && !userFilterRequestDTO.getEmail().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + userFilterRequestDTO.getEmail().toLowerCase() + "%"));
            }
            if (userFilterRequestDTO.getRole() != null) {
                predicates.add(cb.equal(root.get("role"), userFilterRequestDTO.getRole()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}