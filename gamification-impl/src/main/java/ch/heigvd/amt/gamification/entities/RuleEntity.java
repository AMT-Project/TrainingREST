package ch.heigvd.amt.gamification.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class RuleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String eventType;
    private String awardBadge;
    private String awardPoints;
    private int amount;
    private int amountToGet;

    @ManyToOne
    private ApplicationEntity applicationEntity;
}
