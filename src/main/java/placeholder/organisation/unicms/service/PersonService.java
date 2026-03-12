package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.entity.Person;
import placeholder.organisation.unicms.entity.Role;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.repository.PersonRepository;
import placeholder.organisation.unicms.repository.PersonSpecification;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class PersonService {

    PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person findByEmail(String email){
        Person person = personRepository.findByEmail(email).orElseThrow((() -> new EntityNotFoundException(Student.class, email)));
        log.debug("Found Person with email {}", email);
        return person;
    }

    public Optional<Person> findPerson(Long id){
        return personRepository.findById(id);
    }

    public List<Person> findAllPersons(){
        return personRepository.findAll();
    }

    public void changeRole(Long id, Role role) {
        Person person = personRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Person.class, String.valueOf(id)));
        person.setRole(role);
        personRepository.save(person);
        log.debug("Changed role of person {} to {}", id, role);
    }

    public Page<Person> findAll(Pageable pageable) {
        log.debug("Trying to get paginated Person: {}", pageable);
        return personRepository.findAll(pageable);
    }

    public Page<Person> findAllFiltered(String name, String sureName, String email, Role role, Pageable pageable) {
        log.debug("Trying to get filtered Person: name={}, sureName={}, email={}, role={}", name, sureName, email, role);
        return personRepository.findAll(PersonSpecification.filter(name, sureName, email, role), pageable);
    }
}
