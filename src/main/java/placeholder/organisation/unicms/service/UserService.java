package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.User;
import placeholder.organisation.unicms.entity.Role;
import placeholder.organisation.unicms.repository.UserRepository;
import placeholder.organisation.unicms.repository.UserSpecification;
import placeholder.organisation.unicms.service.dto.request.FilterRequestDTO;
import placeholder.organisation.unicms.service.dto.request.UserRequestDTO;
import placeholder.organisation.unicms.service.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email){
        Optional<User> person = userRepository.findByEmail(email);
        log.debug("Found User with email {}", email);
        return person;
    }

    public Optional<User> findPerson(Long id){
        return userRepository.findById(id);
    }

    public List<User> findAllPersons(){
        return userRepository.findAll();
    }

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

    public Page<User> findAllFiltered(FilterRequestDTO filterRequestDTO, Pageable pageable) {
        log.debug("Trying to get filtered User: name={}, sureName={}, email={}, role={}", filterRequestDTO.getEmail(), filterRequestDTO.getSureName(), filterRequestDTO.getEmail(), filterRequestDTO.getRole());
        return userRepository.findAll(UserSpecification.filter(filterRequestDTO), pageable);
    }

    @Transactional
    public void deletePerson(Long id){
        if(!userRepository.existsById(id)){
            throw  new EntityNotFoundException(User.class, String.valueOf(id));
        }
        userRepository.deleteById(id);
        log.debug("Deleted person with id: {}", id);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Transactional
    public User createUser(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.STUFF);
        return userRepository.save(user);
    }
}
