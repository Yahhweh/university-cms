package placeholder.organisation.unicms.service.datagenerator;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.StudySubject;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.StudySubjectService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Log4j2
public class LecturerToStudySubjectGenerator implements DataGenerator {

    private final LecturerService lecturerService;
    private final StudySubjectService subjectService;
    private final int minSubjectsPerLecturer;
    private final int maxSubjectsPerLecturer;
    private final Random random;

    public LecturerToStudySubjectGenerator(LecturerService lecturerService,
                                           StudySubjectService subjectService,
                                           @Value("${subject.generator.min-per-lecturer}") int minSubjectsPerLecturer,
                                           @Value("${subject.generator.max-per-lecturer}") int maxSubjectsPerLecturer) {
        this.lecturerService = lecturerService;
        this.subjectService = subjectService;
        this.minSubjectsPerLecturer = minSubjectsPerLecturer;
        this.maxSubjectsPerLecturer = maxSubjectsPerLecturer;
        this.random = new Random();
    }

    @Override
    public void generate(int amount) {
        log.info("Generating lecturer-to-subject assignments...");

        List<Lecturer> lecturers = lecturerService.findAllLecturers();
        List<StudySubject> allSubjects = subjectService.findAllSubjects();

        if (lecturers.isEmpty() || allSubjects.isEmpty()) {
            log.warn("Seeding aborted: Missing lecturers or subjects in database.");
            return;
        }

        List<Long> allSubjectIds = allSubjects.stream()
                .map(StudySubject::getId)
                .collect(Collectors.toList());

        int limit = Math.min(amount, lecturers.size());

        for (int i = 0; i < limit; i++) {
            Lecturer lecturer = lecturers.get(i);
            int quantity = random.nextInt(maxSubjectsPerLecturer - minSubjectsPerLecturer + 1) + minSubjectsPerLecturer;

            assignSubjects(lecturer.getId(), quantity, allSubjectIds);
        }
    }

    private void assignSubjects(long lecturerId, int quantity, List<Long> sourceSubjectIds) {
        List<Long> availableIds = new ArrayList<>(sourceSubjectIds);

        for (int j = 0; j < quantity; j++) {
            if (availableIds.isEmpty()) {
                break;
            }

            int randomIndex = random.nextInt(availableIds.size());
            Long subjectId = availableIds.get(randomIndex);

            availableIds.remove(randomIndex);

            lecturerService.assignSubjectToLecturer(lecturerId, subjectId);
        }
    }
}