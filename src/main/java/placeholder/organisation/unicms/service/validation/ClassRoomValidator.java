package placeholder.organisation.unicms.service.validation;

import org.springframework.stereotype.Component;
import placeholder.organisation.unicms.entity.Room;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.service.EntityValidationException;

import java.util.List;

@Component
public class ClassRoomValidator {

    private static final List<String> typesForE = List.of("Auditorium", "Conference Room");
    private static final List<String> typesForA = List.of("Hall", "Seminar Room", "Study Room");
    private static final List<String> typesForB = List.of("Laboratory", "Music Room", "Art Studio", "Workshop", "Darkroom");
    private static final List<String> typesForC = List.of("Library", "Study Area", "Working Room");
    private static final List<String> typesForD = List.of("Sport Room");

    public void validateClassRoom(Room room) {
        if (!isClassRoomInCorrectCorpus(room)) {
            throw new EntityValidationException(
                    "Classroom type is not compatible with the assigned corpus letter", Room.class, room.getRoom());
        }
    }

    private boolean isClassRoomInCorrectCorpus(Room classRoom) {
        RoomType roomType = classRoom.getRoomType();
        String room = classRoom.getRoom();
        String realCorpus = String.valueOf(room.charAt(0));

        if (roomType == null) {
            return false;
        }
        if (realCorpus.equals("A") && typesForA.contains(roomType.getName())) {
            return true;
        } else if (realCorpus.equals("B") && typesForB.contains(roomType.getName())) {
            return true;
        } else if (realCorpus.equals("C") && typesForC.contains(roomType.getName())) {
            return true;
        } else if (realCorpus.equals("D") && typesForD.contains(roomType.getName())) {
            return true;
        } else return realCorpus.equals("E") && typesForE.contains(roomType.getName());
    }
}
