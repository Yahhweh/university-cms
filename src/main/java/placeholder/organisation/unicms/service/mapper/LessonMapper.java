package placeholder.organisation.unicms.service.mapper;

import org.mapstruct.*;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.repository.*;
import placeholder.organisation.unicms.service.EntityNotFoundException;
import placeholder.organisation.unicms.service.dto.request.LessonRequestDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
uses = {DurationRepository.class, SubjectRepository.class, RoomRepository.class,
    GroupRepository.class, LecturerRepository.class, })
public interface LessonMapper {

    @Mapping(target = "duration.id", source = "durationId")
    @Mapping(target = "subject.id",source = "studySubjectId")
    @Mapping(target = "group.id", source = "groupId")
    @Mapping(target = "lecturer.id", source = "lecturerId")
    @Mapping(target = "room.id", source = "classRoomId")
    Lesson toEntity(LessonRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "lecturer", ignore = true)
    @Mapping(target = "room", ignore = true)
    void updateEntityFromDto(LessonRequestDTO dto, @MappingTarget Lesson lesson);

    @Mapping(source = "duration.id", target = "durationId")
    @Mapping(source = "subject.id",target = "studySubjectId")
    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "lecturer.id", target = "lecturerId")
    @Mapping(source = "room.id", target = "classRoomId")
    LessonRequestDTO toDto(Lesson lesson);

    default Lesson LessonFromId(Long id, @Context LessonRepository lessonRepository){
        return lessonRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Lesson.class, String.valueOf(id)));
    }
}
