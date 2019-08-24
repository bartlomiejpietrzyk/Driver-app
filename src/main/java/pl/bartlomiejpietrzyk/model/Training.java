package pl.bartlomiejpietrzyk.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String answerA;
    private String answerB;
    private String answerC;
    private String correctAnswer;
    private Integer points;
    @ManyToMany(mappedBy = "trainings")
    private Set<Category> categories;
    @ManyToOne
    private Hint hint;
}
