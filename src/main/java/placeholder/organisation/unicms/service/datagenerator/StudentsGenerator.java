package placeholder.organisation.unicms.service.datagenerator;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.AddressService;
import placeholder.organisation.unicms.service.GroupService;
import placeholder.organisation.unicms.service.StudentService;

import java.time.LocalDate;
import java.util.*;

@Service
@Log4j2
public class StudentsGenerator implements DataGenerator {

    private static final List<String> FIRST_NAMES = List.of(
            "Adrian", "Bastien", "Cillian", "Dante", "Elian",
            "Fabian", "Gideon", "Hugo", "Isaias", "Julian",
            "Kasper", "Leopold", "Maxim", "Nicolas", "Oscar",
            "Phineas", "Quentin", "Raphael", "Soren", "Tristan"
    );

    private static final List<String> LAST_NAMES = List.of(
            "Adler", "Becker", "Chen", "Dubois", "Esposito",
            "Ferreira", "Gorski", "Hansen", "Ito", "Jensen",
            "Kovacs", "Larsen", "Moretti", "Novak", "O'Sullivan",
            "Petrov", "Quinn", "Rossi", "Schmidt", "Tanaka"
    );

    private final StudentService studentService;
    private final GroupService groupService;
    private final AddressService addressService;
    private final Random random = new Random();

    public StudentsGenerator(StudentService studentService, GroupService groupService, AddressService addressService) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.addressService = addressService;
    }

    @Override
    public void generate(int amount) {
        log.info("Generating students...");
        Degree[] degrees = Degree.values();

        List<Address> availableAddresses = addressService.findAll();

        if (availableAddresses.isEmpty()) {
            log.warn("No addresses found in database. Generation might fail if address is mandatory.");
        }

        for (int i = 0; i < amount; i++) {
            Student student = new Student();

            student.setName(FIRST_NAMES.get(random.nextInt(FIRST_NAMES.size())));
            student.setSureName(LAST_NAMES.get(random.nextInt(LAST_NAMES.size())));
            student.setPassword("student_pass_" + i);
            student.setGender(random.nextBoolean() ? GenderType.Male : GenderType.Female);
            student.setDateOfBirth(LocalDate.now().minusYears(18 + random.nextInt(7)));
            student.setEmail(generateEmail(student.getName(), student.getSureName()));

            if (!availableAddresses.isEmpty()) {
                Address randomAddress = availableAddresses.get(random.nextInt(availableAddresses.size()));
                student.setAddress(randomAddress);
            }

            student.setDegree(degrees[random.nextInt(degrees.length)]);

            studentService.createStudent(student);
        }
    }

    public void assignRandomGroups(int minStudents, int maxStudents) {
        log.info("Assigning students to random groups...");
        List<Group> groups = groupService.findAllGroups();
        List<Student> allStudents = studentService.findAllStudents();

        Collections.shuffle(allStudents);
        Queue<Student> studentQueue = new ArrayDeque<>(allStudents);

        for (Group group : groups) {
            if (studentQueue.isEmpty()) break;

            int count = random.nextInt(maxStudents - minStudents + 1) + minStudents;

            for (int i = 0; i < count; i++) {
                Student student = studentQueue.poll();
                if (student == null) break;

                student.setGroup(group);
                studentService.updateStudent(student);
            }
        }
    }

    private static String generateEmail(String name, String sureName) {
        if (name == null || sureName == null) {
            throw new IllegalArgumentException("Parameters for email generation cannot be null");
        }

        String emailName = name.trim().toLowerCase().replaceAll("\\s+", "");
        String emailSureName = sureName.trim().toLowerCase().replaceAll("\\s+", "");
        String emailStudent = "student";
        Integer randomNumber = new Random().nextInt(800);
        String domain = "university.com";
        return String.format("%s.%s%d@%s.%s", emailName, emailSureName, randomNumber, emailStudent, domain);
    }
}