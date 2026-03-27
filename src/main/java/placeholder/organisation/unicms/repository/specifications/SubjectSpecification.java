package placeholder.organisation.unicms.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import placeholder.organisation.unicms.entity.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectSpecification {

    public static Specification<Subject> filter(String name){
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(name != null && !name.isBlank()){
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
