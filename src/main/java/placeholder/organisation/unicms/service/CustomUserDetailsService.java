package placeholder.organisation.unicms.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.entity.Person;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.repository.PersonRepository;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PersonService personService;

    public CustomUserDetailsService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        Person person = personService.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
            return new User(person.getEmail(),
                person.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + person.getRole().name())));
        }
    }
