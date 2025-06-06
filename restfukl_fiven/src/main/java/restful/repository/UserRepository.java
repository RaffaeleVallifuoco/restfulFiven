package restful.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import restful.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // User findById();

    List<User> findByCfContainingIgnoreCase(String cf);

}