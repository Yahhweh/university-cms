package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.repository.RoomTypeRepository;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.service.dto.RoomTypeDTO;
import placeholder.organisation.unicms.service.mapper.ClassRoomTypeMapper;

import java.util.List;
import java.util.Optional;


@Service
@Log4j2
@Transactional(readOnly = true)
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;
    private final ClassRoomTypeMapper classRoomTypeMapper;
    private final  FilterAndSorterOfEntities filterAndSorterOfEntities;

    public RoomTypeService(RoomTypeRepository roomTypeRepository, ClassRoomTypeMapper classRoomTypeMapper,
                           FilterAndSorterOfEntities filterAndSorterOfEntities) {
        this.roomTypeRepository = roomTypeRepository;
        this.classRoomTypeMapper = classRoomTypeMapper;
        this.filterAndSorterOfEntities = filterAndSorterOfEntities;
    }

    public List<RoomType> findAllRoomTypes() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        log.debug("Found {} classroom types ", roomTypes.size());
        return roomTypes;
    }

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

    @Transactional
    public void removeClassRoomType(long classRoomId) {
        if (!roomTypeRepository.existsById(classRoomId)) {
            throw new EntityNotFoundException(RoomType.class, String.valueOf(classRoomId));
        }
        roomTypeRepository.deleteById(classRoomId);
    }

    @Transactional
    public void updateClassRoomType(long classRoomTypeId, RoomTypeDTO roomTypeDTO) {
        RoomType roomType = roomTypeRepository.findById(classRoomTypeId)
                .orElseThrow(() -> new EntityNotFoundException(RoomType.class, String.valueOf(classRoomTypeId)));

        classRoomTypeMapper.updateEntityFromDto(roomTypeDTO, roomType);
        roomTypeRepository.save(roomType);

        log.debug("Classroom type updated successfully. ID: {}", classRoomTypeId);
    }

    public Page<RoomType> getFilteredAndSortedRoomType(String sortField, String sortDir, int pageNo){
        return filterAndSorterOfEntities.getFilteredAndSortedEntities(sortField, sortDir, roomTypeRepository, Specification.where(null), pageNo);
    }
}
