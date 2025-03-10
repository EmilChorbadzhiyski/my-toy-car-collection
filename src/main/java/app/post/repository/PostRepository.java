package app.post.repository;

import app.post.model.Post;
import app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    public List<Post> findByIsVisible(boolean isVisible);
    List<Post> findByOwner(User user);
}
