package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import placeholder.organisation.unicms.entity.Address;
import placeholder.organisation.unicms.entity.GenderType;
import placeholder.organisation.unicms.entity.User;
import placeholder.organisation.unicms.entity.Role;
import placeholder.organisation.unicms.repository.UserRepository;
import placeholder.organisation.unicms.service.dto.request.UserRequestDTO;
import placeholder.organisation.unicms.service.mapper.UserMapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository mockPersonRepository;
    @Mock
    UserMapper mockUserMapper;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    @Test
    void changeRole() {
        User user = getPerson();
        User expectedUser = getPerson();
        Role role = Role.ADMIN;
        expectedUser.setRole(Role.ADMIN);
        when(mockPersonRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.changeRole(user.getId(), role);

        assertThat(expectedUser).isEqualTo(user);
        verify(mockPersonRepository).save(user);
    }

    @Test
    void deleteUser() {

    }

    @Test
    void deletePerson_shouldDeletePerson_whenUserExists() {
        when(mockPersonRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(mockPersonRepository).deleteById(1L);
    }

    @Test
    void deletePerson_shouldThrowEntityNotFoundException_whenUserNotFound() {
        when(mockPersonRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
            () -> userService.deleteUser(99L));

        verify(mockPersonRepository, never()).deleteById(any());
    }

    @Test
    void createUser_shouldSaveUserWithEncodedPasswordAndStaffRole() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("John");
        dto.setSureName("Doe");
        dto.setPassword("plaintext");
        dto.setGender(GenderType.Male);
        dto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        dto.setEmail("john.doe@example.com");

        User mappedUser = new User();
        when(mockUserMapper.toEntity(dto)).thenReturn(mappedUser);
        when(passwordEncoder.encode("plaintext")).thenReturn("encoded");
        when(mockPersonRepository.save(mappedUser)).thenReturn(mappedUser);

        userService.createUser(dto);

        assertThat(mappedUser.getPassword()).isEqualTo("encoded");
        assertThat(mappedUser.getRole()).isEqualTo(Role.STAFF);
        verify(mockPersonRepository).save(mappedUser);
    }

    private User getPerson(){
        return new User(1L, "10fdifjowhef", "John", Role.LECTURER,
            "Pork", GenderType.Male, "john.pork@lecturer.university.com",
            new Address(), LocalDate.now(), "Lecturer");
    }
}