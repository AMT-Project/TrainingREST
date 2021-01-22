package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.ApplicationsApi;
import ch.heigvd.amt.gamification.api.model.Application;
import ch.heigvd.amt.gamification.api.model.ApplicationRegistration;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.repositories.ApplicationRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.util.UUID;

@Controller
public class ApplicationsApiController implements ApplicationsApi {

    @Autowired
    ApplicationRepository applicationRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> registerApplication(@ApiParam(value = "", required = true) @Valid @RequestBody ApplicationRegistration applicationRegistration) {
        ApplicationEntity newApplicationEntity = toApplicationEntity(applicationRegistration);
        applicationRepository.save(newApplicationEntity);

        return ResponseEntity.status(HttpStatus.CREATED).header("X-API-KEY", newApplicationEntity.getApiKey()).build();
    }

    private ApplicationEntity toApplicationEntity(ApplicationRegistration applicationRegistration) {
        ApplicationEntity entity = new ApplicationEntity();
        String apiKey = UUID.randomUUID().toString();
        entity.setApiKey(apiKey);
        entity.setName(applicationRegistration.getName());
        return entity;
    }

    private Application toApplication(ApplicationEntity entity) {
        Application application = new Application();
        application.setName(entity.getName());
        application.setApiKey(entity.getApiKey());
        return application;
    }

}
