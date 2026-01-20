package placeholder.organisation.unicms.service.datagenerator;

import placeholder.organisation.unicms.service.*;

public class LessonGenerator implements DataGenerator{
    DurationService durationService;
    StudySubjectService subjectService;
    GroupService groupService;
    LecturerService lecturerService;
    ClassRoomService classRoomService;
    LessonService lessonService;

    public LessonGenerator(DurationService durationService, StudySubjectService subjectService, GroupService groupService, LecturerService lecturerService, ClassRoomService classRoomService, LessonService lessonService) {
        this.durationService = durationService;
        this.subjectService = subjectService;
        this.groupService = groupService;
        this.lecturerService = lecturerService;
        this.classRoomService = classRoomService;
        this.lessonService = lessonService;
    }

    @Override
    public void generate(int amount) {

    }
}
