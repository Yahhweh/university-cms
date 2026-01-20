package placeholder.organisation.unicms.service.datagenerator;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.entity.ClassRoomType;
import placeholder.organisation.unicms.service.ClassRoomService;
import placeholder.organisation.unicms.service.ClassRoomTypeService;

import java.util.List;
import java.util.Random;

@Log4j2
@Service
public class ClassRoomGenerator implements DataGenerator {

    public static final int MAX_FLOORS = 5;
    public static final int ROOMS_PER_FLOOR = 10;
    private static final int BIG_ROOM_THRESHOLD = 50;
    private static final String LARGE_PREFIX = "A-";
    private static final String SMALL_PREFIX = "B-";

    private final ClassRoomService classRoomService;
    private final ClassRoomTypeService classRoomTypeService;
    private final Random random = new Random();

    public ClassRoomGenerator(ClassRoomService classRoomService,
                              ClassRoomTypeService classRoomTypeService) {
        this.classRoomService = classRoomService;
        this.classRoomTypeService = classRoomTypeService;
    }

    @Override
    public void generate(int amount) {
        log.info("Generating classrooms...");
        List<ClassRoomType> types = classRoomTypeService.findAllRoomTypes();

        if (types.isEmpty()) {
            log.error("No ClassRoomTypes found. Generate types first.");
            return;
        }

        for (int i = 0; i < amount; i++) {
            ClassRoomType selectedType = types.get(random.nextInt(types.size()));

            int floor = (i % MAX_FLOORS) + 1;
            int roomOnFloor = (i / MAX_FLOORS) + 1;

            ClassRoom classRoom = new ClassRoom();
            classRoom.setRoom(formatRoomNumber(selectedType, floor, roomOnFloor));
            classRoom.setClassRoomType(selectedType);

            classRoomService.createClassRoom(classRoom);
        }
    }

    private String formatRoomNumber(ClassRoomType type, int floor, int number) {
        String prefix = (type.getCapacity() > BIG_ROOM_THRESHOLD) ? LARGE_PREFIX : SMALL_PREFIX;
        return String.format("%s%d%02d", prefix, floor, number);
    }
}