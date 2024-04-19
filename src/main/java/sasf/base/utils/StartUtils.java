package sasf.base.utils;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sasf.base.entity.Role;
import sasf.base.entity.User;
import sasf.base.entity.UserRole;
import sasf.base.repository.RoleRepo;
import sasf.base.repository.UserRepo;
import sasf.base.repository.UserRoleRepo;

@Service
public class StartUtils {
    @Autowired
    private RoleRepo _roleRep;

    @Autowired
    private UserRepo _userRep;

    @Autowired
    private UserRoleRepo _userRoleRep;

    @Autowired
    private PasswordEncoder _encoder;

    @PostConstruct
    @Transactional
    public void createInitialRoles() {

        try {

            if (_roleRep.existsByName("ROLE_ADMIN") || _roleRep.existsByName("ROLE_USER")
                    || _roleRep.existsByName("ROLE_MOD")) {
                return;
            }

            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setDescription("Usuario administrador dedicado al sistema");

            Role modRole = new Role();
            modRole.setName("ROLE_MOD");
            modRole.setDescription("Moderador de la aplicación, ayudante de administrador");

            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setDescription("Usuario de la aplicación");

            _roleRep.save(adminRole);
            _roleRep.save(modRole);
            _roleRep.save(userRole);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear los roles iniciales: " + e.getMessage());
        }
    }

    @PostConstruct
    @Transactional
    public void createAdmin() {

        try {

            if (_userRep.existsByUsername("admin")) {
                return;
            }

            Set<Role> adminRole = _roleRep.findByName("ROLE_ADMIN");
            User adminUser = new User();

            adminUser.setId(0l);
            adminUser.setUsername("admin");
            adminUser.setPassword(_encoder.encode("Admin#12345"));
            adminUser.setEmail("admin@admin.com");
            adminUser.setFirstname("AdministratorF");
            adminUser.setLastname("AdministratorL");
            adminUser.setDni("1234567890");

            User administrador = _userRep.save(adminUser);

            UserRole userRole = new UserRole();
            userRole.setUser(administrador);
            userRole.setRole(adminRole.iterator().next());
            _userRoleRep.save(userRole);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear el usuario administrador: " + e.getMessage());
        }
    }
}