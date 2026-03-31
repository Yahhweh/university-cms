package placeholder.organisation.unicms.repository.specifications;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import placeholder.organisation.unicms.entity.Role;
import placeholder.organisation.unicms.entity.User;
import placeholder.organisation.unicms.service.dto.request.filter.UserFilter;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> filter(UserFilter userFilter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userFilter.getName() != null && !userFilter.getName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + userFilter.getName().toLowerCase() + "%"));
            }
            if (userFilter.getSureName() != null && !userFilter.getSureName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("sureName")), "%" + userFilter.getSureName().toLowerCase() + "%"));
            }
            if (userFilter.getEmail() != null && !userFilter.getEmail().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + userFilter.getEmail().toLowerCase() + "%"));
            }
            if (userFilter.getDType() != null) {
                predicates.add(cb.equal(root.get("dtype"), userFilter.getDType()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<User> hasTypeIn(List<?> roles) {
        return (root, query, cb) -> root.get("dtype").in(roles);
    }
}