package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import placeholder.organisation.unicms.entity.Role;
import placeholder.organisation.unicms.entity.User;
import placeholder.organisation.unicms.repository.UserRepository;
import placeholder.organisation.unicms.repository.specifications.UserSpecification;
import placeholder.organisation.unicms.service.dto.request.filter.UserFilter;
import placeholder.organisation.unicms.service.dto.request.UserRequestDTO;
import placeholder.organisation.unicms.service.mapper.UserMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Validated
public class UserService {

    private static final List<String> STAFF_VISIBLE_TYPES = List.of("Student", "Lecturer");

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email){
        Optional<User> user = userRepository.findByEmail(email);
        log.debug("Found User with email {}", email);
        return user;
    }

    public Optional<User> findUser(Long id){
        return userRepository.findById(id);
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void changeRole(Long id, Role role) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, String.valueOf(id)));
        user.setRole(role);
        userRepository.save(user);
        log.debug("Changed role of user {} to {}", id, role);
    }

    public Page<User> findAll(Pageable pageable) {
        log.debug("Trying to get paginated User: {}", pageable);
        return userRepository.findAll(pageable);
    }

    public Page<User> findUsersForAccess(UserFilter dto, Pageable pageable,
                                         boolean isAdmin){
        if(isAdmin){
            return findAllFiltered(dto, pageable);
        }else {
            return (findAllFilteredByType(dto, pageable, STAFF_VISIBLE_TYPES));
        }
    }

    public Page<User> findAllFiltered(UserFilter userFilter, Pageable pageable) {
        log.debug("Trying to get filtered User: name={}, sureName={}, email={}, role={}", userFilter.getName(), userFilter.getSureName(), userFilter.getEmail(), userFilter.getRole());
        return userRepository.findAll(UserSpecification.filter(userFilter), pageable);
    }

    public Page<User> findAllFilteredByType(UserFilter dto, Pageable pageable, List<String> types) {
        return userRepository.findAll(UserSpecification.filter(dto).and(UserSpecification.hasTypeIn(types)), pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw  new EntityNotFoundException(User.class, String.valueOf(id));
        }
        userRepository.deleteById(id);
        log.debug("Deleted user with id: {}", id);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public User createUser(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.STAFF);
        return userRepository.save(user);
    }
}
