package placeholder.organisation.unicms.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import placeholder.organisation.unicms.entity.Person;
import placeholder.organisation.unicms.entity.Role;

import java.util.ArrayList;
import java.util.List;

public class PersonSpecification {

    public static Specification<Person> filter(String name, String sureName, String email, Role role) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (sureName != null && !sureName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("sureName")), "%" + sureName.toLowerCase() + "%"));
            }
            if (email != null && !email.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (role != null) {
                predicates.add(cb.equal(root.get("role"), role));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}