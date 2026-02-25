package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.repository.RoomRepository;
import placeholder.organisation.unicms.repository.RoomTypeRepository;
import placeholder.organisation.unicms.service.dto.RoomDTO;
import placeholder.organisation.unicms.service.mapper.ClassRoomMapper;
import placeholder.organisation.unicms.service.validation.ClassRoomValidator;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final ClassRoomValidator classRoomValidator;
    private final ClassRoomMapper classRoomMapper;
    private final RoomTypeRepository roomTypeRepository;
    private final FilterAndSorterOfEntities filterAndSorterOfEntities;

    public RoomService(RoomRepository roomRepository, ClassRoomValidator classRoomValidator,
                       ClassRoomMapper classRoomMapper, RoomTypeRepository roomTypeRepository,
                       FilterAndSorterOfEntities filterAndSorterOfEntities) {
        this.roomRepository = roomRepository;
        this.classRoomValidator = classRoomValidator;
        this.classRoomMapper = classRoomMapper;
        this.roomTypeRepository = roomTypeRepository;
        this.filterAndSorterOfEntities = filterAndSorterOfEntities;
    }

    public List<Room> findAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        log.debug("Found {} classrooms", rooms.size());
        return rooms;
    }

    @Transactional
    public void createClassRoom(Room room) {
        classRoomValidator.validateClassRoom(room);

        roomRepository.save(room);
        log.debug("Classroom saved successfully: {}", room.getRoom());
    }

    public Optional<Room> findClassRoomByName(String classRoomName) {
        return roomRepository.findByRoom(classRoomName);
    }

    public Optional<Room> findClassRoom(long classRoomId) {
        return roomRepository.findById(classRoomId);
    }

    @Transactional
    public void removeClassRoom(long classRoomId) {
        if (!roomRepository.existsById(classRoomId)) {
            throw new EntityNotFoundException(Room.class, String.valueOf(classRoomId));
        }
        roomRepository.deleteById(classRoomId);
    }

    @Transactional
    public void updateClassRoom(long classRoomId, RoomDTO roomDTO) {
        Room room = roomRepository.findById(classRoomId)
                .orElseThrow(() -> new EntityNotFoundException(Room.class, String.valueOf(classRoomId)));

        classRoomMapper.updateEntityFromDto(roomDTO, room);
        resolveRelations(roomDTO, room);

        classRoomValidator.validateClassRoom(room);
        roomRepository.save(room);
        log.debug("Classroom updated successfully. ID: {}", classRoomId);
    }

    public Page<Room> getFilteredAndSortedRoom(String sortField, String sortDir, int pageNo) {
        return filterAndSorterOfEntities.getFilteredAndSortedEntities(sortField, sortDir, roomRepository, Specification.where(null), pageNo);
    }

    void resolveRelations(RoomDTO dto, Room entity) {
        if (dto.getClassRoomTypeId() != null) {
            entity.setRoomType(roomTypeRepository.getReferenceById(dto.getClassRoomTypeId()));
        }
    }
}