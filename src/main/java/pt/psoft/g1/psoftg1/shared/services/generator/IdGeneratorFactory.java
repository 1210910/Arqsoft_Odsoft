package pt.psoft.g1.psoftg1.shared.services.generator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
    public class IdGeneratorFactory {

    public IdGenerator getGenerator() {
        return ApplicationContextProvider.getApplicationContext().getBean(IdGenerator.class);

    }
}

