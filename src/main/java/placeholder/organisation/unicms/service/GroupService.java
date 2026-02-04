package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.repository.GroupRepository;
import placeholder.organisation.unicms.entity.Group;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> findAllGroups() {
        List<Group> groups = groupRepository.findAll();
        log.debug("Found {} groups", groups.size());
        return groups;
    }

    @Transactional
    public void createGroup(Group group) {
        groupRepository.save(group);
        log.debug("Group saved successfully. Name: {}", group.getName());
    }

    public Optional<Group> findGroup(long id) {
        Optional<Group> group = groupRepository.findById(id);
        group.ifPresent(value -> log.debug("Found group {}", value));
        return group;
    }

    @Transactional
    public void removeGroup(long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new ServiceException("Group not found with id: " + groupId);
        }
        groupRepository.deleteById(groupId);
    }
}