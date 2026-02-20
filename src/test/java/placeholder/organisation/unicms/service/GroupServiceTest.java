package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.repository.GroupRepository;
import placeholder.organisation.unicms.service.dto.GroupDTO;
import placeholder.organisation.unicms.service.mapper.GroupMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    @Spy
    GroupMapper groupMapper = Mappers.getMapper(GroupMapper.class);
    @Mock
    GroupRepository groupRepository;
    @InjectMocks
    GroupService groupService;

    @Test
    void updateGroup_shouldSave_whenCorrectGroup() {
        Group initial = getGroup();
        GroupDTO changes = getGroupDTO();
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
    Group getGroup() {
        return new Group(1L, "A-122");
    }

    GroupDTO getGroupDTO() {
        return new GroupDTO("G-1");
    }

}