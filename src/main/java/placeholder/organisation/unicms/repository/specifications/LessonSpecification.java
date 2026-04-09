    package placeholder.organisation.unicms.repository.specifications;

    import jakarta.persistence.criteria.Predicate;
    import org.springframework.data.jpa.domain.Specification;
    import placeholder.organisation.unicms.entity.Lesson;
    import placeholder.organisation.unicms.service.dto.request.filter.LessonFilter;

    import java.util.ArrayList;
    import java.util.List;

    public class LessonSpecification {

        public static Specification<Lesson> filter(LessonFilter filter){
            return ((root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();

                if(filter.getDurationId() != null){
                    predicates.add(cb.equal(root.get("duration").get("id"), filter.getDurationId()));
                }
                if(filter.getSubject() != null && !filter.getSubject().isBlank()){
                    predicates.add(cb.like(cb.lower(root.get("subject").get("name")), "%" + filter.getSubject().toLowerCase() + "%"));
                }
                if(filter.getGroup() != null && !filter.getGroup().isBlank()){
                    predicates.add(cb.like(cb.lower(root.get("group").get("name")), "%" + filter.getGroup().toLowerCase() + "%"));
                }
                if(filter.getRoom() != null && !filter.getRoom().isBlank()){
                    predicates.add(cb.like(cb.lower(root.get("room").get("room")), "%" + filter.getRoom().toLowerCase() + "%"));
                }
                if(filter.getLecturer().getName() != null && !filter.getLecturer().getName().isBlank()){
                    predicates.add(cb.like(cb.lower(root.get("lecturer").get("name")), "%" + filter.getLecturer().getName().toLowerCase() + "%"));
                }
                if(filter.getLecturer().getSureName() != null && !filter.getLecturer().getSureName().isBlank()){
                    predicates.add(cb.like(cb.lower(root.get("lecturer").get("sureName")), "%" + filter.getLecturer().getSureName().toLowerCase() + "%"));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            });
        }
    }