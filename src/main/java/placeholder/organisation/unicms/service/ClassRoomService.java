package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.ClassRoomType;
import placeholder.organisation.unicms.repository.ClassRoomRepository;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.repository.ClassRoomTypeRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Log4j2
@Transactional(readOnly = true)
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;

    private static final List<String> typesForE = List.of("Auditorium", "Conference Room");
    private static final List<String> typesForA = List.of("Hall", "Seminar Room", "Study Room");
    private static final List<String> typesForB = List.of("Laboratory", "Music Room", "Art Studio", "Workshop", "Darkroom");
    private static final List<String> typesForC = List.of("Library", "Study Area", "Working Room");
    private static final List<String> typesForD = List.of("Sport Room");


    public ClassRoomService(ClassRoomRepository classRoomRepository) {
        this.classRoomRepository = classRoomRepository;

    }

    public List<ClassRoom> findAllRooms() {
        try {
            List<ClassRoom> classRooms = classRoomRepository.findAll();
            log.debug("Found {} classrooms ", classRooms.size());
            return classRooms;
        } catch (RuntimeException e) {
            log.error("Database error while fetching classrooms", e);
            throw new ServiceException("Error retrieving rooms", e);
        }
    }

    @Transactional
    public void createClassRoom(ClassRoom classRoom) {
        if(!isClassRoomInCorrectCorpus(classRoom)) throw  new ServiceException("Error in validation of classroom");
        classRoomRepository.save(classRoom);
        log.debug("Classroom saved successfully. Name: {}}", classRoom.getRoom());
    }

    public Optional<ClassRoom> findClassRoomByName(String classRoomName) {
        try {
            Optional<ClassRoom> classRoom = classRoomRepository.findByRoom(classRoomName);
            classRoom.ifPresent(value -> log.debug("Found classroom by name {}", value));
            return classRoom;
        } catch (RuntimeException e) {
            log.error("Database error while searching for classroom name: {}", classRoomName, e);
            throw new ServiceException("Error finding lesson", e);
        }
    }

    public Optional<ClassRoom> findClassRoom(long classRoomId) {
        try {
            Optional<ClassRoom> classRoom = classRoomRepository.findById(classRoomId);
            classRoom.ifPresent(value -> log.debug("Found classroom {}", value));
            return classRoom;
        } catch (RuntimeException e) {
            log.error("Database error while searching for classroom  id: {}", classRoomId, e);
            throw new ServiceException("Error finding classroom", e);
        }
    }

    @Transactional
    public void removeClassRoom(long classRoomId){
        try {
            Optional<ClassRoom> classRoom = classRoomRepository.findById(classRoomId);
            classRoom.ifPresent(classRoomRepository::delete);
        }catch (RuntimeException e){
            log.error("Failed to delete classroom with id: {}", classRoomId);
            throw new ServiceException("Error deleting classroom");
        }
    }

    private boolean isClassRoomInCorrectCorpus(ClassRoom classRoom){
        ClassRoomType classRoomType = classRoom.getClassRoomType();
        String room = classRoom.getRoom();
        String realCorpus = String.valueOf(room.charAt(0));

        if(classRoomType == null) return false;
        if(realCorpus.equals("A") && typesForA.contains(classRoomType.getName())) return true;
        else if(realCorpus.equals("B") && typesForB.contains(classRoomType.getName())) return true;
        else if(realCorpus.equals("C") && typesForC.contains(classRoomType.getName())) return true;
        else if(realCorpus.equals("D") && typesForD.contains(classRoomType.getName())) return true;
        else if(realCorpus.equals("E") && typesForE.contains(classRoomType.getName())) return true;
        else return false;
    }

}