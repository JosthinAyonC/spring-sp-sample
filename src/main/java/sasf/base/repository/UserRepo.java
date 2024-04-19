package sasf.base.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import sasf.base.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.status = true ORDER BY u.id DESC")
    Page<User> findAllActiveUsers(Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.id = ?1 AND u.status = true")
    boolean userExistsById(Long id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.username = ?1 AND u.status = true")
    boolean existsByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = ?1 AND u.status = true")
    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.dni = ?1 AND u.status = true")
    boolean existsByDni(String dni);

    @Modifying
    @Query("UPDATE User u SET u.status = false WHERE u.id = ?1")
    void deactivateUserById(Long id);

    @Query("SELECT u FROM User u WHERE u.username = ?1 OR u.email = ?2 AND u.status = true")
    Optional<User> findByUsernameOrEmail(String usernameOrEmail, String usernameOrEmail2);

    @Query("SELECT u FROM User u WHERE u.username = ?1 AND u.status = true")
    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = ?1 AND u.status = true")
    User findByEmail(String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.dni = ?1 AND u.status = true")
    boolean userExistsByDni(String id);

    @Query("SELECT u FROM User u WHERE u.dni = ?1 AND u.status = true")
    User findByDni(String id);
}
