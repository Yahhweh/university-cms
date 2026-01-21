package placeholder.organisation.unicms.service.datagenerator;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class LessonGenerator implements DataGenerator {

    private final DurationService durationService;
    private final StudySubjectService subjectService;
    private final GroupService groupService;
    private final LecturerService lecturerService;
    private final ClassRoomService classRoomService;
    private final LessonService lessonService;

    private final Random random = new Random();

    private static final int WEEKS_TO_GENERATE = 4;
    private static final int MIN_LESSONS_PER_DAY = 2;
    private static final int MAX_LESSONS_PER_DAY = 4;
    private static final LocalDate START_DATE = LocalDate.now();

    public LessonGenerator(DurationService durationService,
                           StudySubjectService subjectService,
                           GroupService groupService,
                           LecturerService lecturerService,
                           ClassRoomService classRoomService,
                           LessonService lessonService) {
        this.durationService = durationService;
        this.subjectService = subjectService;
        this.groupService = groupService;
        this.lecturerService = lecturerService;
        this.classRoomService = classRoomService;
        this.lessonService = lessonService;
    }

    @Override
    public void generate(int amount) {
        log.info("Starting lesson generation for {} weeks...", WEEKS_TO_GENERATE);

        List<Duration> durations = durationService.findAllDurations();
        List<Group> groups = groupService.findAllGroups();
        List<ClassRoom> classRooms = classRoomService.findAllRooms();
        List<Lecturer> lecturers = lecturerService.findAllLecturers();

        if (durations.isEmpty() || groups.isEmpty() || classRooms.isEmpty() || lecturers.isEmpty()) {
            log.error("Cannot generate lessons: missing required data");
            return;
        }

        int lessonsGenerated = 0;
        LocalDate currentDate = START_DATE;
        LocalDate endDate = START_DATE.plusWeeks(WEEKS_TO_GENERATE);

        for (Group group : groups) {
            lessonsGenerated += generateScheduleForGroup(
                    group,
                    currentDate,
                    endDate,
                    durations,
                    classRooms,
                    lecturers
            );
        }

        log.info("Lesson generation completed. Total lessons created: {}", lessonsGenerated);
    }

    private int generateScheduleForGroup(Group group,
                                         LocalDate startDate,
                                         LocalDate endDate,
                                         List<Duration> durations,
                                         List<ClassRoom> classRooms,
                                         List<Lecturer> lecturers) {
        int count = 0;
        LocalDate currentDate = startDate;

        Map<StudySubject, List<Lecturer>> subjectToLecturers = buildSubjectLecturerMap(lecturers);

        if (subjectToLecturers.isEmpty()) {
            log.warn("No subjects with assigned lecturers found for group: {}", group.getName());
            return 0;
        }

        List<StudySubject> availableSubjects = new ArrayList<>(subjectToLecturers.keySet());

        while (currentDate.isBefore(endDate)) {
            if (isWeekend(currentDate)) {
                currentDate = currentDate.plusDays(1);
                continue;
            }

            int lessonsPerDay = random.nextInt(MAX_LESSONS_PER_DAY - MIN_LESSONS_PER_DAY + 1)
                    + MIN_LESSONS_PER_DAY;

            Set<Long> usedDurationIds = new HashSet<>();
            Set<Long> usedClassRoomIds = new HashSet<>();
            Set<Long> usedLecturerIds = new HashSet<>();

            for (int i = 0; i < lessonsPerDay && i < durations.size(); i++) {
                try {
                    Duration duration = selectAvailableDuration(durations, usedDurationIds);
                    if (duration == null) break;

                    StudySubject subject = availableSubjects.get(random.nextInt(availableSubjects.size()));

                    Lecturer lecturer = selectAvailableLecturer(
                            subjectToLecturers.get(subject),
                            usedLecturerIds
                    );
                    if (lecturer == null) continue;

                    ClassRoom classRoom = selectAvailableClassRoom(classRooms, usedClassRoomIds);
                    if (classRoom == null) continue;

                    Lesson lesson = new Lesson(
                            duration,
                            subject,
                            group,
                            lecturer,
                            classRoom,
                            currentDate
                    );

                    lessonService.addLesson(lesson);

                    usedDurationIds.add(duration.getId());
                    usedClassRoomIds.add(classRoom.getId());
                    usedLecturerIds.add(lecturer.getId());

                    count++;

                } catch (Exception e) {
                    log.error("Error creating lesson for group {} on {}: {}",
                            group.getName(), currentDate, e.getMessage());
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        log.debug("Generated {} lessons for group {}", count, group.getName());
        return count;
    }

    private Map<StudySubject, List<Lecturer>> buildSubjectLecturerMap(List<Lecturer> lecturers) {
        Map<StudySubject, List<Lecturer>> map = new HashMap<>();

        for (Lecturer lecturer : lecturers) {
            if (lecturer.getStudySubjects() != null && !lecturer.getStudySubjects().isEmpty()) {
                for (StudySubject subject : lecturer.getStudySubjects()) {
                    map.computeIfAbsent(subject, k -> new ArrayList<>()).add(lecturer);
                }
            }
        }

        return map;
    }

    private Duration selectAvailableDuration(List<Duration> durations, Set<Long> usedIds) {
        List<Duration> available = durations.stream()
                .filter(d -> !usedIds.contains(d.getId()))
                .collect(Collectors.toList());

        return available.isEmpty() ? null : available.get(random.nextInt(available.size()));
    }

    private Lecturer selectAvailableLecturer(List<Lecturer> lecturers, Set<Long> usedIds) {
        if (lecturers == null || lecturers.isEmpty()) return null;

        List<Lecturer> available = lecturers.stream()
                .filter(l -> !usedIds.contains(l.getId()))
                .collect(Collectors.toList());

        return available.isEmpty() ? null : available.get(random.nextInt(available.size()));
    }

    private ClassRoom selectAvailableClassRoom(List<ClassRoom> classRooms, Set<Long> usedIds) {
        List<ClassRoom> available = classRooms.stream()
                .filter(c -> !usedIds.contains(c.getId()))
                .collect(Collectors.toList());

        return available.isEmpty() ? null : available.get(random.nextInt(available.size()));
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}