package placeholder.organisation.unicms.service.datagenerator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.*;

import java.time.LocalTime;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonGeneratorTest {

    @Mock
    private DurationService durationService;
    @Mock
    private StudySubjectService subjectService;
    @Mock
    private GroupService groupService;
    @Mock
    private LecturerService lecturerService;
    @Mock
    private ClassRoomService classRoomService;
    @Mock
    private LessonService lessonService;

    private LessonGenerator lessonGenerator;

    @BeforeEach
    void setUp() {
        lessonGenerator = new LessonGenerator(
                durationService,
                subjectService,
                groupService,
                lecturerService,
                classRoomService,
                lessonService
        );
    }

    @Test
    void generate_createsLessons_whenAllDataIsAvailable() {
        List<Duration> durations = createDurations(3);
        List<Group> groups = createGroups(2);
        List<ClassRoom> classRooms = createClassRooms(5);
        List<Lecturer> lecturers = createLecturersWithSubjects(3);

        when(durationService.findAllDurations()).thenReturn(durations);
        when(groupService.findAllGroups()).thenReturn(groups);
        when(classRoomService.findAllRooms()).thenReturn(classRooms);
        when(lecturerService.findAllLecturers()).thenReturn(lecturers);

        lessonGenerator.generate(4);

        verify(lessonService, atLeastOnce()).addLesson(any(Lesson.class));
    }

    @Test
    void generate_doesNotCreateLessons_whenDurationsAreMissing() {
        when(durationService.findAllDurations()).thenReturn(Collections.emptyList());
        when(groupService.findAllGroups()).thenReturn(createGroups(2));
        when(classRoomService.findAllRooms()).thenReturn(createClassRooms(5));
        when(lecturerService.findAllLecturers()).thenReturn(createLecturersWithSubjects(3));

        lessonGenerator.generate(4);

        verify(lessonService, never()).addLesson(any(Lesson.class));
    }

    @Test
    void generate_doesNotCreateLessons_whenGroupsAreMissing() {
        when(durationService.findAllDurations()).thenReturn(createDurations(3));
        when(groupService.findAllGroups()).thenReturn(Collections.emptyList());
        when(classRoomService.findAllRooms()).thenReturn(createClassRooms(5));
        when(lecturerService.findAllLecturers()).thenReturn(createLecturersWithSubjects(3));

        lessonGenerator.generate(4);

        verify(lessonService, never()).addLesson(any(Lesson.class));
    }

    @Test
    void generate_doesNotCreateLessons_whenClassRoomsAreMissing() {
        when(durationService.findAllDurations()).thenReturn(createDurations(3));
        when(groupService.findAllGroups()).thenReturn(createGroups(2));
        when(classRoomService.findAllRooms()).thenReturn(Collections.emptyList());
        when(lecturerService.findAllLecturers()).thenReturn(createLecturersWithSubjects(3));

        lessonGenerator.generate(4);

        verify(lessonService, never()).addLesson(any(Lesson.class));
    }

    @Test
    void generate_doesNotCreateLessons_whenLecturersAreMissing() {
        when(durationService.findAllDurations()).thenReturn(createDurations(3));
        when(groupService.findAllGroups()).thenReturn(createGroups(2));
        when(classRoomService.findAllRooms()).thenReturn(createClassRooms(5));
        when(lecturerService.findAllLecturers()).thenReturn(Collections.emptyList());

        lessonGenerator.generate(4);

        verify(lessonService, never()).addLesson(any(Lesson.class));
    }

    @Test
    void generate_createsMultipleLessons_whenMultipleGroupsExist() {
        List<Duration> durations = createDurations(4);
        List<Group> groups = createGroups(3);
        List<ClassRoom> classRooms = createClassRooms(10);
        List<Lecturer> lecturers = createLecturersWithSubjects(5);

        when(durationService.findAllDurations()).thenReturn(durations);
        when(groupService.findAllGroups()).thenReturn(groups);
        when(classRoomService.findAllRooms()).thenReturn(classRooms);
        when(lecturerService.findAllLecturers()).thenReturn(lecturers);

        lessonGenerator.generate(4);

        verify(lessonService, atLeast(groups.size())).addLesson(any(Lesson.class));
    }

    @Test
    void generate_handlesLecturersWithoutSubjects() {
        List<Duration> durations = createDurations(3);
        List<Group> groups = createGroups(1);
        List<ClassRoom> classRooms = createClassRooms(5);
        List<Lecturer> lecturersWithoutSubjects = createLecturersWithoutSubjects(2);

        when(durationService.findAllDurations()).thenReturn(durations);
        when(groupService.findAllGroups()).thenReturn(groups);
        when(classRoomService.findAllRooms()).thenReturn(classRooms);
        when(lecturerService.findAllLecturers()).thenReturn(lecturersWithoutSubjects);

        lessonGenerator.generate(4);

        verify(lessonService, never()).addLesson(any(Lesson.class));
    }

    @Test
    void generate_createsLessonsWithVariousResources() {
        int durationCount = 5;
        int groupCount = 2;
        int classRoomCount = 8;
        int lecturerCount = 4;

        List<Duration> durations = createDurations(durationCount);
        List<Group> groups = createGroups(groupCount);
        List<ClassRoom> classRooms = createClassRooms(classRoomCount);
        List<Lecturer> lecturers = createLecturersWithSubjects(lecturerCount);

        when(durationService.findAllDurations()).thenReturn(durations);
        when(groupService.findAllGroups()).thenReturn(groups);
        when(classRoomService.findAllRooms()).thenReturn(classRooms);
        when(lecturerService.findAllLecturers()).thenReturn(lecturers);

        lessonGenerator.generate(4);

        verify(lessonService, atLeast(groupCount * 2)).addLesson(any(Lesson.class));
    }

    private List<Duration> createDurations(int count) {
        List<Duration> durations = new ArrayList<>();
        LocalTime start = LocalTime.of(8, 30);

        for (int i = 1; i <= count; i++) {
            Duration d = new Duration();
            d.setId((long) i);
            d.setStart(start);
            d.setEnd(start.plusMinutes(90));
            durations.add(d);
            start = start.plusMinutes(105);
        }

        return durations;
    }

    private List<Group> createGroups(int count) {
        List<Group> groups = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            Group g = new Group();
            g.setId((long) i);
            g.setName("Group-" + i);
            groups.add(g);
        }
        return groups;
    }

    private List<ClassRoom> createClassRooms(int count) {
        List<ClassRoom> classRooms = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            ClassRoom c = new ClassRoom();
            c.setId((long) i);
            c.setRoom("Room-" + i);
            classRooms.add(c);
        }
        return classRooms;
    }

    private List<Lecturer> createLecturersWithSubjects(int count) {
        List<Lecturer> lecturers = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Lecturer l = new Lecturer();
            l.setId((long) i);
            l.setName("Lecturer-" + i);

            Set<StudySubject> subjects = new HashSet<>();
            StudySubject s1 = new StudySubject((long) i, "Subject-" + i + "A");
            StudySubject s2 = new StudySubject((long) (i + 10), "Subject-" + i + "B");
            subjects.add(s1);
            subjects.add(s2);

            l.setStudySubjects(subjects);
            lecturers.add(l);
        }

        return lecturers;
    }

    private List<Lecturer> createLecturersWithoutSubjects(int count) {
        List<Lecturer> lecturers = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            Lecturer l = new Lecturer();
            l.setId((long) i);
            l.setName("Lecturer-" + i);
            l.setStudySubjects(new HashSet<>());
            lecturers.add(l);
        }

        return lecturers;
    }
}