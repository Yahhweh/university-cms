package placeholder.organisation.unicms.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import placeholder.organisation.unicms.entity.User;
import placeholder.organisation.unicms.service.dto.request.FilterRequestDTO;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> filter(FilterRequestDTO filterRequestDTO) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterRequestDTO.getName() != null && !filterRequestDTO.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filterRequestDTO.getName().toLowerCase() + "%"));
            }
            if (filterRequestDTO.getSureName() != null && !filterRequestDTO.getSureName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("sureName")), "%" + filterRequestDTO.getSureName().toLowerCase() + "%"));
            }
            if (filterRequestDTO.getEmail() != null && !filterRequestDTO.getEmail().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + filterRequestDTO.getEmail().toLowerCase() + "%"));
            }
            if (filterRequestDTO.getRole() != null) {
                predicates.add(cb.equal(root.get("role"), filterRequestDTO.getRole()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}