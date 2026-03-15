package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.Person;
import placeholder.organisation.unicms.entity.Role;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.repository.PersonRepository;
import placeholder.organisation.unicms.repository.PersonSpecification;
import placeholder.organisation.unicms.service.dto.request.FilterRequestDTO;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class PersonService {

    PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Optional<Person> findByEmail(String email){
        Optional<Person> person = personRepository.findByEmail(email);
        log.debug("Found Person with email {}", email);
        return person;
    }

    public Optional<Person> findPerson(Long id){
        return personRepository.findById(id);
    }

    public List<Person> findAllPersons(){
        return personRepository.findAll();
    }

    @Transactional
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

    public Page<Person> findAllFiltered(FilterRequestDTO filterRequestDTO, Pageable pageable) {
        log.debug("Trying to get filtered Person: name={}, sureName={}, email={}, role={}", filterRequestDTO.getEmail(), filterRequestDTO.getSureName(), filterRequestDTO.getEmail(), filterRequestDTO.getRole());
        return personRepository.findAll(PersonSpecification.filter(filterRequestDTO), pageable);
    }

    @Transactional
    public void deletePerson(Long id){
        if(!personRepository.existsById(id)){
            throw  new EntityNotFoundException(Person.class, String.valueOf(id));
        }
        personRepository.deleteById(id);
        log.debug("Deleted person with id: {}", id);
    }
}
