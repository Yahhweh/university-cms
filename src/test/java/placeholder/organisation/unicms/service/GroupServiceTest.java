package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Course;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.repository.CourseRepository;
import placeholder.organisation.unicms.repository.GroupRepository;
import placeholder.organisation.unicms.service.dto.request.GroupRequestDTO;
import placeholder.organisation.unicms.service.mapper.GroupMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    @Spy
    GroupMapper groupMapper = Mappers.getMapper(GroupMapper.class);
    @Mock
    GroupRepository groupRepository;
    @Mock
    CourseRepository courseRepository;
    @InjectMocks
    GroupService groupService;

    @Test
    void updateGroup_shouldSave_whenCorrectGroup() {
        Group initial = getGroup();
        GroupRequestDTO changes = getGroupDTO();
        long id = initial.getId();

        when(groupRepository.findById(id)).thenReturn(Optional.of(initial));

        groupService.updateGroup(id, changes);

        verify(groupMapper).updateEntityFromDto(changes, initial);
        verify(groupRepository).save(initial);

        assertThat(initial.getName()).isEqualTo("G-1");
    }

    @Test
    void createGroup_shouldSave_whenCorrectGroupGiven() {
        Group group = getGroup();

        assertDoesNotThrow(() -> groupService.createGroup(group));

        verify(groupRepository).save(group);
    }

    @Test
    void removeGroup_shouldRemoveGroup_WhenGroupExists() {
        Group group = getGroup();

        when(groupRepository.existsById(group.getId())).thenReturn(true);

        groupService.removeGroup(group.getId());

        verify(groupRepository).deleteById(group.getId());
    }

    @Test
    void removeGroup_shouldThrowEntityNotFound_WhenGroupDoesNotExist() {
        long id = 22L;

        when(groupRepository.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> groupService.removeGroup(id));
        verify(groupRepository).existsById(id);
    }
    @Test
    void updateCourseGroups_addsAndRemovesGroups_differentially() {
        Course course = getCourse();
        Group groupA = new Group(1L, "A", course);
        Group groupB = new Group(2L, "B", course);
        Group groupC = new Group(3L, "C", null);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(groupRepository.findByCourse(course)).thenReturn(List.of(groupA, groupB));
        when(groupRepository.findById(3L)).thenReturn(Optional.of(groupC));

        groupService.updateCourseGroups(1L, List.of(1L, 3L));

        assertThat(groupA.getCourse()).isEqualTo(course);
        assertThat(groupB.getCourse()).isNull();
        assertThat(groupC.getCourse()).isEqualTo(course);
        verify(groupRepository, never()).findById(1L);
    }

    @Test
    void updateCourseGroups_throwsEntityNotFound_whenCourseNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> groupService.updateCourseGroups(99L, List.of(1L)));
    }

    Group getGroup() {
        return new Group(1L, "A-122", getCourse());
    }

    private Course getCourse(){
        return new Course(1L, "SE", List.of(new Subject()));
    }

    GroupRequestDTO getGroupDTO() {
        return new GroupRequestDTO("G-1");
    }

}