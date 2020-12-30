package ch.heigvd.amt.gamification.api.endpoints;

import ch.heigvd.amt.gamification.api.RulesApi;
import ch.heigvd.amt.gamification.api.model.*;
import ch.heigvd.amt.gamification.entities.ApplicationEntity;
import ch.heigvd.amt.gamification.entities.RuleEntity;
import ch.heigvd.amt.gamification.repositories.RuleRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.net.URI;

@Controller
public class RulesApiController implements RulesApi {
    @Autowired
    ServletRequest request;

    @Autowired
    RuleRepository ruleRepository;

    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createRule(@ApiParam(value = "", required = true) @Valid @RequestBody Rule rule) {
        RuleEntity newRuleEntity = toRuleEntity(rule);
        newRuleEntity.setApplicationEntity((ApplicationEntity) request.getAttribute("applicationEntity"));
        ruleRepository.save(newRuleEntity);

        Long id = newRuleEntity.getId();

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(newRuleEntity.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    private RuleEntity toRuleEntity(Rule rule) {
        RuleEntity entity = new RuleEntity();
        entity.setName(rule.getName());
        entity.setEventType(rule.getIf().getEventType());
        entity.setAwardBadge(rule.getThen().getAwardBadge());
        entity.setAwardPoints(rule.getThen().getAwardPoints().getPointScale());
        entity.setAmount(rule.getThen().getAwardPoints().getAmount());
        entity.setAmountToGet(rule.getThen().getAwardPoints().getAmountToGet());
        return entity;
    }

    private Rule toRule(RuleEntity entity) {
        Rule rule = new Rule();
        rule.setName(entity.getName());

        RuleIf ruleIf = new RuleIf();
        rule.setIf(ruleIf.eventType(entity.getEventType()));

        RuleThen ruleThen = new RuleThen();
        ruleThen.setAwardBadge(entity.getAwardBadge());

        RuleThenAwardPoints ruleThenAwardPoints = new RuleThenAwardPoints();
        ruleThenAwardPoints.setPointScale(entity.getAwardPoints());
        ruleThenAwardPoints.setAmount(entity.getAmount());
        ruleThenAwardPoints.setAmountToGet(entity.getAmountToGet());
        ruleThen.setAwardPoints(ruleThenAwardPoints);

        return rule;
    }
}
