package pl.bartlomiejpietrzyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bartlomiejpietrzyk.model.Hint;

@Repository
public interface HintRepository extends JpaRepository<Hint, Long> {
}
