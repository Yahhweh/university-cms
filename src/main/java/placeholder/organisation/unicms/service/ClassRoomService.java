package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.repository.ClassRoomRepository;
import placeholder.organisation.unicms.entity.ClassRoom;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;

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

    public void removeClassRoom(long classRoomId){
        try {
            Optional<ClassRoom> classRoom = classRoomRepository.findById(classRoomId);
            classRoom.ifPresent(classRoomRepository.delete(classRoom));
        }catch (RuntimeException e){
            log.error("Failed to delete classroom with id: {}", classRoomId);
            throw new ServiceException("Error deleting classroom");
        }
    }
}