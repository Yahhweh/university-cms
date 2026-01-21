package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.dao.ClassRoomDao;
import placeholder.organisation.unicms.entity.ClassRoom;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class ClassRoomService {
    ClassRoomDao classRoomDao;

    public ClassRoomService(ClassRoomDao classRoomDao) {
        this.classRoomDao = classRoomDao;
    }

    public List<ClassRoom> findAllRooms() {
        try {
            List<ClassRoom> classRooms = classRoomDao.findAll();
            log.debug("Found {} classrooms ", classRooms.size());
            return classRooms;
        } catch (RuntimeException e) {
            log.error("Database error while fetching classrooms", e);
            throw new ServiceException("Error retrieving rooms", e);
        }
    }

    @Transactional
    public void createClassRoom(ClassRoom classRoom) {
        classRoomDao.save(classRoom);
        log.debug("Classroom saved successfully. Name: {}}", classRoom.getRoom());
    }

    public Optional<ClassRoom> findClassRoomByName(String classRoomName) {
        try {
            Optional<ClassRoom> classRoom = classRoomDao.findByRoom(classRoomName);
            classRoom.ifPresent(value -> log.debug("Found classroom by name {}", value));
            return classRoom;
        } catch (RuntimeException e) {
            log.error("Database error while searching for classroom name: {}", classRoomName, e);
            throw new ServiceException("Error finding lesson", e);
        }
    }

    public Optional<ClassRoom> findClassRoom(long classRoomId) {
        try {
            Optional<ClassRoom> classRoom = classRoomDao.findById(classRoomId);
            classRoom.ifPresent(value -> log.debug("Found classroom {}", value));
            return classRoom;
        } catch (RuntimeException e) {
            log.error("Database error while searching for classroom  id: {}", classRoomId, e);
            throw new ServiceException("Error finding classroom", e);
        }
    }
}