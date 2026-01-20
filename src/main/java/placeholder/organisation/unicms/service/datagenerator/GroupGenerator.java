package placeholder.organisation.unicms.service.datagenerator;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.service.GroupService;

import java.util.Random;

@Service
@Log4j2
public class GroupGenerator implements DataGenerator {

    private final static int ALPHABET_SIZE = 26;
    private final static int RANDOM_NUMBER_LIMIT = 10;

    private final GroupService groupService;

    public GroupGenerator(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public void generate(int amount) {
        log.info("Generating groups...");
        for (int i = 0; i < amount; i++) {
            Group group = new Group();
            group.setName(generateGroupName());
            groupService.createGroup(group);
        }
    }

    private String generateGroupName() {
        Random random = new Random();

        char ch1 = (char) ('A' + random.nextInt(ALPHABET_SIZE));
        char ch2 = (char) ('A' + random.nextInt(ALPHABET_SIZE));

        return ch1 + "" + ch2 + "-" + random.nextInt(RANDOM_NUMBER_LIMIT) + random.nextInt(RANDOM_NUMBER_LIMIT);
    }
}