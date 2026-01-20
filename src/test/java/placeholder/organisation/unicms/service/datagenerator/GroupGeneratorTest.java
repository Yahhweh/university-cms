package placeholder.organisation.unicms.service.datagenerator;

import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.service.GroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GroupGeneratorTest {

    @Mock
    private GroupService mockGroupService;

    @InjectMocks
    private GroupGenerator groupsGenerator;

    private static final int groupsAmount = 10;

    @Test
    void generate_shouldCreateExactAmountOfGroups() {
        groupsGenerator.generate(groupsAmount);

        verify(mockGroupService, times(groupsAmount)).createGroup(any(Group.class));
    }

    @Test
    void generate_shouldCreateNothing_whenAmountIsZero() {
        groupsGenerator.generate(0);

        verify(mockGroupService, never()).createGroup(any(Group.class));
    }
}