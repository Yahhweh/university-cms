package placeholder.organisation.unicms.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.Student;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    StudentService studentService;
    LecturerService lecturerService;

    public CustomUserDetailsService(StudentService studentService, LecturerService lecturerService) {
        this.studentService = studentService;
        this.lecturerService = lecturerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            if (username.contains("@lecturer")) {
                return lecturerService.findByEmail(username);
            } else{
                return studentService.findByEmail(username);
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
