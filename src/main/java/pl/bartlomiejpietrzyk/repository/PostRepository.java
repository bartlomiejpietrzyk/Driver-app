package pl.bartlomiejpietrzyk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.bartlomiejpietrzyk.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
