package pl.bartlomiejpietrzyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bartlomiejpietrzyk.model.Thread;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long> {
    Thread findByTitle(String title);
}
