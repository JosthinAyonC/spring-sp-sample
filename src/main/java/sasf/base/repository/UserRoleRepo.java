package sasf.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sasf.base.entity.UserRole;

public interface UserRoleRepo extends JpaRepository<UserRole, Long> {

}
