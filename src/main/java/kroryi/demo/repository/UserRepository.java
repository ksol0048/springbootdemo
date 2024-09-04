package kroryi.demo.repository;

import kroryi.demo.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.username=:username")
    List<User> findByUsername(@Param("username") String username);

    List<User> findByUsernameOrEmailOrderByIdDesc(
            @Param("username") String username,
            @Param("email") String email
    );

    List<User> findByUsernameLikeOrderByIdDesc(String username, Pageable pageable);
}
