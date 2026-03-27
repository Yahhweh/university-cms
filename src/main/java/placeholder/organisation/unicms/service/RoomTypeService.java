package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.repository.RoomTypeRepository;
import placeholder.organisation.unicms.service.dto.request.RoomTypeRequestDTO;
import placeholder.organisation.unicms.service.mapper.ClassRoomTypeMapper;

import java.util.List;
import java.util.Optional;


@Service
@Log4j2
@Transactional(readOnly = true)
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final ClassRoomTypeMapper classRoomTypeMapper;

    public RoomTypeService(RoomTypeRepository roomTypeRepository, ClassRoomTypeMapper classRoomTypeMapper) {
        this.roomTypeRepository = roomTypeRepository;
        this.classRoomTypeMapper = classRoomTypeMapper;
    }

    public List<RoomType> findAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        log.debug("Found {} classroom types ", roomTypes.size());
        return roomTypes;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void createClassroomType(RoomType roomTypeName) {
        roomTypeRepository.save(roomTypeName);
        log.debug("Classroom type saved successfully. Name: {}}", roomTypeName.getName());
    }

    public Optional<RoomType> findClassRoomTypeByName(String classRoomTypeName) {
        Optional<RoomType> classRoomType = roomTypeRepository.findByName(classRoomTypeName);
        classRoomType.ifPresent(value -> log.debug("Found classroom type {}", value));
        return classRoomType;
    }

    public Optional<RoomType> findClassRoomType(long classRoomTypeId) {
        Optional<RoomType> classRoomType = roomTypeRepository.findById(classRoomTypeId);
        classRoomType.ifPresent(value -> log.debug("Found classroom type {}", value));
        return classRoomType;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void removeClassRoomType(long classRoomId) {
        if (!roomTypeRepository.existsById(classRoomId)) {
            throw new EntityNotFoundException(RoomType.class, String.valueOf(classRoomId));
        }
        roomTypeRepository.deleteById(classRoomId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void updateClassRoomType(long classRoomTypeId, RoomTypeRequestDTO roomTypeRequestDTO) {
        RoomType roomType = roomTypeRepository.findById(classRoomTypeId)
            .orElseThrow(() -> new EntityNotFoundException(RoomType.class, String.valueOf(classRoomTypeId)));

        classRoomTypeMapper.updateEntityFromDto(roomTypeRequestDTO, roomType);
        roomTypeRepository.save(roomType);

        log.debug("Classroom type updated successfully. ID: {}", classRoomTypeId);
    }

    public Page<RoomType> findAll(Pageable pageable) {
        log.debug("Trying to get paginated RoomTypes: {}", pageable);
        return roomTypeRepository.findAll(pageable);
    }
}
