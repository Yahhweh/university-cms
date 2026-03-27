package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.repository.RoomRepository;
import placeholder.organisation.unicms.repository.RoomTypeRepository;
import placeholder.organisation.unicms.repository.specifications.RoomSpecification;
import placeholder.organisation.unicms.service.dto.request.RoomRequestDTO;
import placeholder.organisation.unicms.service.dto.request.filter.RoomFilterRequestDTO;
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

    public RoomService(RoomRepository roomRepository, ClassRoomValidator classRoomValidator,
                       ClassRoomMapper classRoomMapper, RoomTypeRepository roomTypeRepository) {
        this.roomRepository = roomRepository;
        this.classRoomValidator = classRoomValidator;
        this.classRoomMapper = classRoomMapper;
        this.roomTypeRepository = roomTypeRepository;
    }

    public List<Room> findAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        log.debug("Found {} classrooms", rooms.size());
        return rooms;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void createClassRoom(Room room) {
        classRoomValidator.validateClassRoom(room);

        roomRepository.save(room);
        log.debug("Classroom saved successfully: {}", room.getRoom());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void createRoom(RoomRequestDTO dto) {
        Room room = new Room();
        classRoomMapper.updateEntityFromDto(dto, room);
        resolveRelations(dto, room);
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

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void removeClassRoom(long classRoomId) {
        if (!roomRepository.existsById(classRoomId)) {
            throw new EntityNotFoundException(Room.class, String.valueOf(classRoomId));
        }
        roomRepository.deleteById(classRoomId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void updateClassRoom(long classRoomId, RoomRequestDTO roomRequestDTO) {
        Room room = roomRepository.findById(classRoomId)
            .orElseThrow(() -> new EntityNotFoundException(Room.class, String.valueOf(classRoomId)));

        classRoomMapper.updateEntityFromDto(roomRequestDTO, room);
        resolveRelations(roomRequestDTO, room);

        classRoomValidator.validateClassRoom(room);
        roomRepository.save(room);
        log.debug("Classroom updated successfully. ID: {}", classRoomId);
    }

    public Page<Room> findAll(RoomFilterRequestDTO requestDTO, Pageable pageable) {
        log.debug("Trying to get paginated Rooms: {}", pageable);
        return roomRepository.findAll(RoomSpecification.filter(requestDTO), pageable);
    }

    void resolveRelations(RoomRequestDTO dto, Room entity) {
        if (dto.getClassRoomTypeId() != null) {
            entity.setRoomType(roomTypeRepository.getReferenceById(dto.getClassRoomTypeId()));
        }
    }
}