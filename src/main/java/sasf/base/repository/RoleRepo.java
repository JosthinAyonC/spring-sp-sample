package sasf.base.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sasf.base.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Role r WHERE r.name = ?1 AND r.status = true")
    boolean existsByName(String name);

    @Query("SELECT r FROM Role r WHERE r.name = :name AND r.status = true")
    Set<Role> findByName(@Param("name") String name);
}
