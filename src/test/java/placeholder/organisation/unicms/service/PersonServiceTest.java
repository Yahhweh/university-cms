package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;
import placeholder.organisation.unicms.entity.Address;
import placeholder.organisation.unicms.entity.GenderType;
import placeholder.organisation.unicms.entity.Person;
import placeholder.organisation.unicms.entity.Role;
import placeholder.organisation.unicms.repository.PersonRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    PersonRepository mockPersonRepository;
    @InjectMocks
    PersonService personService;

    @Test
    void changeRole() {
        Person person = getPerson();
        Person expectedPerson = getPerson();
        Role role = Role.ADMIN;
        expectedPerson.setRole(Role.ADMIN);
        when(mockPersonRepository.findById(person.getId())).thenReturn(Optional.of(person));

        personService.changeRole(person.getId(), role);

        assertThat(expectedPerson).isEqualTo(person);
        verify(mockPersonRepository).save(person);
    }

    @Test
    void deletePerson() {

    }

    @Test
    void deletePerson_shouldDeletePerson_whenPersonExists() {
        when(mockPersonRepository.existsById(1L)).thenReturn(true);

        personService.deletePerson(1L);

        verify(mockPersonRepository).deleteById(1L);
    }

    @Test
    void deletePerson_shouldThrowEntityNotFoundException_whenPersonNotFound() {
        when(mockPersonRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
            () -> personService.deletePerson(99L));

        verify(mockPersonRepository, never()).deleteById(any());
    }

    private Person getPerson(){
        return new Person(1L, "10fdifjowhef", "John", Role.LECTURER,
            "Pork", GenderType.Male, "john.pork@lecturer.university.com",
            new Address(), LocalDate.now(), "Lecturer");
    }
}