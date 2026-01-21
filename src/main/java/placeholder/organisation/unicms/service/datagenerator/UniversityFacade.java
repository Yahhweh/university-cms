package placeholder.organisation.unicms.service.datagenerator;


import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.service.StudentService;

@Service
@Log4j2
public class UniversityFacade {
    private static final int SUBJECT_AMOUNT = 10;
    private static final int STUDENT_AMOUNT = 150;
    private static final int GROUPS_AMOUNT = 20;
    private static final int LECTURER_AMOUNT = 13;
    private static final int ADDRESS_AMOUNT = STUDENT_AMOUNT + LECTURER_AMOUNT;
    private static final int CLASSROOM_TYPE_AMOUNT = 15;
    private static final int CLASSROOM_AMOUNT = 50;
    private static final int DURATION_AMOUNT = 6;

    private static final int MIN_STUDENTS_IN_GROUP = 10;
    private static final int MAX_STUDENTS_IN_GROUP = 30;

    private final StudentService studentService;
    private final DbCleaningService dbCleaningService;
    private final AddressGenerator addressGenerator;
    private final ClassRoomTypeGenerator classRoomTypeGenerator;
    private final ClassRoomGenerator classRoomGenerator;
    private final DurationGenerator durationGenerator;
    private final GroupGenerator groupGenerator;
    private final LecturerGenerator lecturerGenerator;
    private final LecturerToStudySubjectGenerator lecturerToStudySubjectGenerator;
    private final LessonGenerator lessonGenerator;
    private final StudentsGenerator studentsGenerator;
    private final StudySubjectGenerator studySubjectGenerator;

    public UniversityFacade(AddressGenerator addressGenerator, ClassRoomTypeGenerator classRoomTypeGenerator, ClassRoomGenerator classRoomGenerator, DurationGenerator durationGenerator, GroupGenerator groupGenerator, LecturerGenerator lecturerGenerator, LecturerToStudySubjectGenerator lecturerToStudySubjectGenerator, LessonGenerator lessonGenerator, StudentsGenerator studentsGenerator, StudySubjectGenerator studySubjectGenerator, StudentService studentService, DbCleaningService dbCleaningService) {
        this.addressGenerator = addressGenerator;
        this.dbCleaningService = dbCleaningService;
        this.classRoomTypeGenerator = classRoomTypeGenerator;
        this.classRoomGenerator = classRoomGenerator;
        this.durationGenerator = durationGenerator;
        this.groupGenerator = groupGenerator;
        this.lecturerGenerator = lecturerGenerator;
        this.lecturerToStudySubjectGenerator = lecturerToStudySubjectGenerator;
        this.lessonGenerator = lessonGenerator;
        this.studentsGenerator = studentsGenerator;
        this.studySubjectGenerator = studySubjectGenerator;
        this.studentService = studentService;
    }

    @Transactional
    public void initializeDataBase(){
        if(studentService.findAllStudents().size() >= 50){
            log.info("Database already has enough data. Skipping initialization part...");
            return;
        }
        dbCleaningService.deleteAll();
        log.info("Generating data");

        addressGenerator.generate(ADDRESS_AMOUNT);
        classRoomTypeGenerator.generate(CLASSROOM_TYPE_AMOUNT);
        classRoomGenerator.generate(CLASSROOM_AMOUNT);
        durationGenerator.generate(DURATION_AMOUNT);
        groupGenerator.generate(GROUPS_AMOUNT);
        studySubjectGenerator.generate(SUBJECT_AMOUNT);
        lecturerGenerator.generate(LECTURER_AMOUNT);
        lecturerToStudySubjectGenerator.generate(LECTURER_AMOUNT);
        studentsGenerator.generate(STUDENT_AMOUNT);
        studentsGenerator.assignRandomGroups(MIN_STUDENTS_IN_GROUP, MAX_STUDENTS_IN_GROUP);

        //does not depend on amount (formula in class)
        lessonGenerator.generate(0);
    }

}
