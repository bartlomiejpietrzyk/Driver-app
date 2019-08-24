package pl.bartlomiejpietrzyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadRepository extends JpaRepository<Thread, Long> {
    Thread findByTitle(String title);
}
