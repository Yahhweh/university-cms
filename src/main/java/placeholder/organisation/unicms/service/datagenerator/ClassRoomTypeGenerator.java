package placeholder.organisation.unicms.service.datagenerator;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.entity.ClassRoomType;
import placeholder.organisation.unicms.service.ClassRoomTypeService;

import java.util.List;

@Service
@Log4j2
public class ClassRoomTypeGenerator implements DataGenerator {

    private static final List<String> NAMES = List.of(
            "Lecture Hall", "Seminar Room", "Computer Lab", "Physics Lab",
            "Chemistry Lab", "Biology Lab", "Art Studio", "Music Room",
            "Gymnasium", "Conference Room", "Library Hall", "Study Room",
            "Workshop", "Darkroom", "Auditorium"
    );

    private final ClassRoomTypeService classRoomTypeService;

    public ClassRoomTypeGenerator(ClassRoomTypeService classRoomTypeService) {
        this.classRoomTypeService = classRoomTypeService;
    }

    @Override
    public void generate(int amount) {
        log.info("Generating ClassRoomTypes...");
        for (int i = 0; i < amount; i++) {
            String typeName = NAMES.get(i % NAMES.size());

            ClassRoomType type = new ClassRoomType();
            type.setName(typeName);
            type.setCapacity(typeName.contains("Hall") ? 100L : 30L);

            classRoomTypeService.createClassroomType(type);
        }
    }
}