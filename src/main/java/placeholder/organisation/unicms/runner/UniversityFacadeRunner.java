package placeholder.organisation.unicms.runner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import placeholder.organisation.unicms.service.datagenerator.UniversityFacade;

@Component
@Order(1)
public class UniversityFacadeRunner implements ApplicationRunner {

    UniversityFacade universityFacade;

    public UniversityFacadeRunner(UniversityFacade universityFacade) {
        this.universityFacade = universityFacade;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        universityFacade.initializeDataBase();
    }
}
