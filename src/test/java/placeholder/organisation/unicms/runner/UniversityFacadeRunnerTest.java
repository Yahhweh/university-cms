package placeholder.organisation.unicms.runner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import placeholder.organisation.unicms.service.datagenerator.UniversityFacade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UniversityFacadeRunnerTest {

    @Mock
    private UniversityFacade universityFacade;
    @InjectMocks
    private UniversityFacadeRunner universityFacadeRunner;

    @Test
    void run_callUniversityFacadeMethod_WhenCalling() throws Exception {
        ApplicationArguments applicationArguments = new DefaultApplicationArguments();

        universityFacadeRunner.run(applicationArguments);
        verify(universityFacade).initializeDataBase();

    }
}