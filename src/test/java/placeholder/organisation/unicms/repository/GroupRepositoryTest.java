package placeholder.organisation.unicms.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.entity.Subject;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest(
    includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GroupRepository.class)
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/datasets/group_jpa.sql",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class GroupRepositoryTest {

    @Autowired
    GroupRepository groupRepository;

    @Test
    void findDistinctByCourseSubjectsIn_shouldReturnGroups_whenSubjectsMatch() {
        Subject subject = new Subject(1L, "Java");
        int expectedGroupsSize = 1;

        List<Group> groups = groupRepository.findDistinctByCourseSubjectsIn(Set.of(subject));

        assertThat(groups).isNotNull();
        assertThat(groups.size()).isEqualTo(expectedGroupsSize);
        assertThat(groups.get(0).getName()).isEqualTo("Group-A");
    }

    @Test
    void findDistinctByCourseSubjectsIn_shouldReturnEmpty_whenNoSubjectsMatch() {
        Subject subject = new Subject(99L, "NonExistent");

        List<Group> groups = groupRepository.findDistinctByCourseSubjectsIn(Set.of(subject));

        assertThat(groups).isEqualTo(List.of());
    }
}