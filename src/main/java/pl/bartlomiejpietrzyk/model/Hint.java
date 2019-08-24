package pl.bartlomiejpietrzyk.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
public class Hint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "hint_category", joinColumns = @JoinColumn(name = "hint_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "hint_threads",
            joinColumns = @JoinColumn(name = "hint_id"),
            inverseJoinColumns = @JoinColumn(name = "thread_id"))
    private Set<Thread> threads;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "hint_trainings",
            joinColumns = @JoinColumn(name = "hint_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id"))
    private Set<Training> trainings;
}
