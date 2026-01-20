package placeholder.organisation.unicms.service.datagenerator;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.entity.StudySubject;
import placeholder.organisation.unicms.service.StudySubjectService;

import java.util.List;

@Service
@Log4j2
public class StudySubjectGenerator implements DataGenerator {

    private static final List<String> subjects = List.of(
            "Mathematics",
            "Physics",
            "Software Engineering",
            "Procedural Programming",
            "Discrete Math",
            "Introduction To Speciality",
            "Information Technologies",
            "Philosophy",
            "English Language",
            "Computer Graphics"
    );

    private final StudySubjectService studySubjectService;

    public StudySubjectGenerator(StudySubjectService studySubjectService) {
        this.studySubjectService = studySubjectService;
    }

    @Override
    public void generate(int amount) {
        log.info("Generating subjects...");
        for (int i = 0; i < amount; i++) {
            studySubjectService.createStudySubject(new StudySubject(subjects.get(i)));
        }
    }
}

