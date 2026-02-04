package placeholder.organisation.unicms.service.createDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonCreateDTO {
    private Long id;
    private DurationCreateDTO duration;
    private StudySubjectCreateDTO studySubject;
    private GroupCreateDTO group;
    private LecturerCreateDTO lecturer;
    private ClassRoomCreateDTO classRoom;
    private LocalDate date;
}