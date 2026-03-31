package placeholder.organisation.unicms.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.service.dto.request.filter.SubjectFilter;

import java.util.ArrayList;
import java.util.List;

public class SubjectSpecification {

    public static Specification<Subject> filter(SubjectFilter filter){
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(filter.getName() != null && !filter.getName().isBlank()){
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.getName().toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}
