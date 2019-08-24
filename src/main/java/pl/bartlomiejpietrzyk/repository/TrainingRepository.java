package pl.bartlomiejpietrzyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bartlomiejpietrzyk.model.Training;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    Training findByTitle(String title);
}
