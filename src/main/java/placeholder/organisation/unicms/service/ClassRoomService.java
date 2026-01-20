package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.dao.ClassRoomJpa;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.entity.ClassRoomType;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class ClassRoomService {
    ClassRoomJpa classRoomJpa;

    public ClassRoomService(ClassRoomJpa classRoomJpa) {
        this.classRoomJpa = classRoomJpa;
    }

    public List<ClassRoom> findAllRooms() {
        List<ClassRoom> classRoomTypes = classRoomJpa.findAll();
        log.debug("Found {} classrooms ", classRoomTypes.size());
        return classRoomTypes;
    }

    @Transactional
    public void createClassRoom(ClassRoom classRoom) {
        classRoomJpa.save(classRoom);
        log.debug("Classroom saved successfully. Name: {}}", classRoom.getRoom());
    }

    public Optional<ClassRoom> findClassRoomByName(String classRoomTypeName) {
        Optional<ClassRoom> classRoom = classRoomJpa.findByRoom(classRoomTypeName);
        classRoom.ifPresent(value -> log.debug("Found classroom {}", value));
        return classRoom;
    }

    public Optional<ClassRoom> findClassRoomType(long ClassRoomId){
        Optional<ClassRoom> classRoom = classRoomJpa.findById(ClassRoomId);
        classRoom.ifPresent(value -> log.debug("Found classroom {}", value));
        return classRoom;
    }
}
