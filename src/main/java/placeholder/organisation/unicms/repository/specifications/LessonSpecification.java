    package placeholder.organisation.unicms.repository.specifications;

    import jakarta.persistence.criteria.Predicate;
    import org.springframework.data.jpa.domain.Specification;
    import placeholder.organisation.unicms.entity.Lesson;
    import placeholder.organisation.unicms.service.dto.request.filter.LessonFilter;

    import java.util.ArrayList;
    import java.util.List;

    public class LessonSpecification {

        public static Specification<Lesson> filter(LessonFilter requestDTO){
            return ((root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();

                if(requestDTO.getDurationId() != null){
                    predicates.add(cb.equal(root.get("duration").get("id"), requestDTO.getDurationId()));
                }
                if(requestDTO.getSubject() != null && !requestDTO.getSubject().isBlank()){
                    predicates.add(cb.like(cb.lower(root.get("subject").get("name")), "%" + requestDTO.getSubject().toLowerCase() + "%"));
                }
                if(requestDTO.getGroup() != null && !requestDTO.getGroup().isBlank()){
                    predicates.add(cb.like(cb.lower(root.get("group").get("name")), "%" + requestDTO.getGroup().toLowerCase() + "%"));
                }
                if(requestDTO.getRoom() != null && !requestDTO.getRoom().isBlank()){
                    predicates.add(cb.like(cb.lower(root.get("room").get("room")), "%" + requestDTO.getRoom().toLowerCase() + "%"));
                }
                if(requestDTO.getLecturer().getName() != null && !requestDTO.getLecturer().getName().isBlank()){
                    predicates.add(cb.like(cb.lower(root.get("lecturer").get("name")), "%" + requestDTO.getLecturer().getName().toLowerCase() + "%"));
                }
                if(requestDTO.getLecturer().getSureName() != null && !requestDTO.getLecturer().getSureName().isBlank()){
                    predicates.add(cb.like(cb.lower(root.get("lecturer").get("sureName")), "%" + requestDTO.getLecturer().getSureName().toLowerCase() + "%"));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            });
        }
    }