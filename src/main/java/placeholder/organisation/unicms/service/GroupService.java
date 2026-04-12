package placeholder.organisation.unicms.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.repository.CourseRepository;
import placeholder.organisation.unicms.repository.GroupRepository;
import placeholder.organisation.unicms.repository.UserRepository;
import placeholder.organisation.unicms.service.dto.request.GroupRequestDTO;
import placeholder.organisation.unicms.service.mapper.GroupMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional(readOnly = true)
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public List<Group> findAllGroups() {
        List<Group> groups = groupRepository.findAll();
        log.debug("Found {} groups", groups.size());
        return groups;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void createGroup(Group group) {
        groupRepository.save(group);
        log.debug("Group saved successfully: {}", group.getName());
    }

    public Optional<Group> findGroup(long id) {
        return groupRepository.findById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Transactional
    public void removeGroup(long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new EntityNotFoundException(Group.class, String.valueOf(groupId));
        }
        groupRepository.deleteById(groupId);
    }

    @PreAuthorize("hasAnyRole('ADMIN, MENTOR, STAFF')")
    @Transactional
    public void updateGroup(long groupId, GroupRequestDTO groupResponseDTO) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new EntityNotFoundException(Group.class, String.valueOf(groupId)));

        groupMapper.updateEntityFromDto(groupResponseDTO, group);
        groupRepository.save(group);

        log.debug("Group updated successfully. ID: {}", groupId);
    }

    public Page<Group> findAll(Pageable pageable) {
        log.debug("Fetching paginated Groups: {}", pageable);
        return groupRepository.findAll(pageable);
    }

    public List<Group> findGroupsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new EntityNotFoundException(Course.class, String.valueOf(courseId)));
        return groupRepository.findByCourse(course);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void updateCourseGroups(Long courseId, List<Long> groupIds) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new EntityNotFoundException(Course.class, String.valueOf(courseId)));

        List<Long> newIds = groupIds != null ? groupIds : List.of();
        List<Group> currentGroups = groupRepository.findByCourse(course);

        currentGroups.stream()
            .filter(g -> !newIds.contains(g.getId()))
            .forEach(g -> g.setCourse(null));

        Set<Long> currentIds = currentGroups.stream().map(Group::getId).collect(Collectors.toSet());
        for (Long id : newIds) {
            if (!currentIds.contains(id)) {
                Group group = groupRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(Group.class, String.valueOf(id)));
                group.setCourse(course);
            }
        }

        log.debug("Updated groups for course {}", courseId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void removeGroupFromCourse(Long groupId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new EntityNotFoundException(Group.class, String.valueOf(groupId)));
        group.setCourse(null);
        groupRepository.save(group);
        log.debug("Removed group {} from course", groupId);
    }

    public List<Long> getGroupsByCourse(Long courseId) {
        return findGroupsByCourse(courseId).stream().map(Group::getId).toList();
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Transactional
    public void assignMentorToGroup(Long mentorId, Long groupId){
        User mentor = userRepository.findById(mentorId)
            .orElseThrow(() -> new EntityNotFoundException(User.class, String.valueOf(mentorId)));
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new EntityNotFoundException(Group.class, String.valueOf(groupId)));

        if(!mentor.getRoles().contains(Role.MENTOR)){
            throw new InsufficientRoleException(User.class, String.valueOf(mentorId));
        }
        group.setMentor(mentor);
        log.debug("Mentor was assigned to group successfully. Mentor ID: {}. Group ID: {}", mentorId, groupId);
    }
}