package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.dao.GroupDao;
import placeholder.organisation.unicms.entity.Group;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class GroupService {

    GroupDao groupDao;

    public GroupService(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public List<Group> findAllGroups() {
        List<Group> groups = groupDao.findAll();
        log.debug("Found {} groups", groups.size());
        return groups;
    }

    @Transactional
    public void createGroup(Group group) {
        groupDao.save(group);
        log.debug("Group saved successfully. Name: {}", group.getName());
    }

    public Optional<Group> findGroup(long id) {
        Optional<Group> group = groupDao.findById(id);
        group.ifPresent(value -> log.debug("Found group {}", value));
        return group;
    }
}