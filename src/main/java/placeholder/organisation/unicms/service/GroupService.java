package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.repository.GroupRepository;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.service.dto.GroupDTO;
import placeholder.organisation.unicms.service.mapper.GroupMapper;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final FilterAndSorterOfEntities filterAndSorterOfEntities;

    public GroupService(GroupRepository groupRepository, GroupMapper groupMapper, FilterAndSorterOfEntities filterAndSorterOfEntities) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.filterAndSorterOfEntities = filterAndSorterOfEntities;
    }

    public List<Group> findAllGroups() {
        List<Group> groups = groupRepository.findAll();
        log.debug("Found {} groups", groups.size());
        return groups;
    }

    @Transactional
    public void createGroup(Group group) {
        groupRepository.save(group);
        log.debug("Group saved successfully: {}", group.getName());
    }

    public Optional<Group> findGroup(long id) {
        return groupRepository.findById(id);
    }

    @Transactional
    public void removeGroup(long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new EntityNotFoundException(Group.class, String.valueOf(groupId));
        }
        groupRepository.deleteById(groupId);
    }

    @Transactional
    public void updateGroup(long groupId, GroupDTO groupDTO) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException(Group.class, String.valueOf(groupId)));

        groupMapper.updateEntityFromDto(groupDTO, group);
        groupRepository.save(group);

        log.debug("Group updated successfully. ID: {}", groupId);
    }

    public Page<Group> getFilteredAndSortedGroup(String sortingField, String sortingDirection, int pageNo){
        return filterAndSorterOfEntities.getFilteredAndSortedEntities(sortingField, sortingDirection, groupRepository, Specification.where(null), pageNo);
    }
}