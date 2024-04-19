package sasf.base.service;

import java.util.Optional;

import javax.validation.Valid;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sasf.base.dto.ChangePasswordRequestDto;
import sasf.base.dto.UserRequestDto;
import sasf.base.dto.UserResponseDto;
import sasf.base.entity.Role;
import sasf.base.entity.User;
import sasf.base.entity.UserRole;
import sasf.base.repository.RoleRepo;
import sasf.base.repository.UserRepo;
import sasf.base.repository.UserRoleRepo;
import sasf.base.utils.Globales;
import sasf.base.utils.Mapper;

@Service
public class UserService {
    @Autowired
    private UserRepo _userRep;
    @Autowired
    private RoleRepo _roleRep;
    @Autowired
    private UserRoleRepo _userRoleRep;
    @Autowired
    private PasswordEncoder _encoder;
    @Autowired
    private Mapper _mapper;

    public Page<UserResponseDto> listAllUsers(Pageable pageable) {
        Page<User> users = _userRep.findAllActiveUsers(pageable);

        if (users.isEmpty()) {
            throw new RuntimeException("No hay usuarios registrados o el numero de registro que busca no existe.");
        }

        Page<UserResponseDto> usersFormat = users.map(user -> {
            UserResponseDto userFormat = _mapper.mapObject(user, UserResponseDto.class);
            user.getUserRoles().forEach(role -> {
                userFormat.getRoles().add(role.getName());
            });
            return userFormat;
        });

        return usersFormat;
    }

    public UserResponseDto getUserById(Long id) {
        if (!_userRep.userExistsById(id)) {
            throw new RuntimeException(
                    String.format("No existe el usuario con id: %s, por favor pruebe con un id existente.", id));
        }
        User user = _userRep.findById(id).get();
        UserResponseDto userFormat = _mapper.mapObject(user, UserResponseDto.class);
        user.getUserRoles().forEach(role -> {
            userFormat.getRoles().add(role.getName());
        });
        return userFormat;
    }

    public UserResponseDto createUser(UserRequestDto user) {
        User newUser = _mapper.mapObject(user, User.class);
        validarPost(newUser);
        newUser = _userRep.save(newUser);
        assignRole(newUser, Globales.ID_USER);
        return _mapper.mapObject(newUser, UserResponseDto.class);
    }

    public UserResponseDto createMod(@Valid UserRequestDto user) {
        User newUser = _mapper.mapObject(user, User.class);
        validarPost(newUser);
        newUser = _userRep.save(newUser);
        assignRole(newUser, Globales.ID_MOD);
        return _mapper.mapObject(newUser, UserResponseDto.class);
    }

    public String changePassword(ChangePasswordRequestDto password) {
        if (!_userRep.userExistsById(password.getId())) {
            throw new RuntimeException(
                    String.format("No existe el usuario con id: %s, por favor pruebe con un id existente.",
                            password.getId()));
        }
        User user = _userRep.findById(password.getId()).get();
        if (!_encoder.matches(password.getOldPassword(), user.getPassword())) {
            throw new RuntimeException(
                    "La contraseña actual no coincide con la contraseña ingresada, pruebe nuevamente.");
        } else {
            user.setPassword(_encoder.encode(password.getNewPassword()));
            _userRep.save(user);
        }
        return "{\"msg\":\"Contraseña editada satisfactoriamente\"}";
    }

    public UserResponseDto getUserByDni(String id) {
        if (!_userRep.userExistsByDni(id)) {
            throw new RuntimeException(
                    String.format("No existe el usuario con dni: %s, por favor pruebe con un dni existente.", id));
        }
        User user = _userRep.findByDni(id);
        UserResponseDto userFormat = _mapper.mapObject(user, UserResponseDto.class);
        user.getUserRoles().forEach(role -> {
            userFormat.getRoles().add(role.getName());
        });
        return userFormat;
    }

    private void validarPost(User user) {
        if (_userRep.existsByUsername(user.getUsername())) {
            throw new DataException("El nombre de usuario ya se encuentra registrado, pruebe con otro.", null);
        }
        if (_userRep.existsByEmail(user.getEmail())) {
            throw new DataException("El email ya se encuentra registrado, pruebe con otro.", null);
        }
        if (_userRep.existsByDni(user.getDni())) {
            throw new DataException("El dni ya se encuentra registrado, pruebe con otro.", null);
        }
        user.setPassword(_encoder.encode(user.getPassword()));
    }

    private void validarPut(User user) {
        Optional<User> userBase = _userRep.findById(user.getId());
        if (!userBase.isPresent()) {
            throw new DataException(
                    String.format("No existe el usuario con id: %s, por favor pruebe con un id existente.",
                            user.getId()),
                    null);
        }
        user.setCreatedAt(userBase.get().getCreatedAt());
        if (user.getFirstname() == null || user.getFirstname().equals("")) {
            user.setFirstname(userBase.get().getFirstname());
        }
        if (user.getLastname() == null || user.getLastname().equals("")) {
            user.setLastname(userBase.get().getLastname());
        }
        if (user.getStatus() == null) {
            user.setStatus(userBase.get().getStatus());
        }
        if (user.getDni() != null && !user.getDni().equals("")) {
            if (_userRep.existsByDni(user.getDni())) {
                if (!user.getDni().equals(userBase.get().getDni())) {
                    throw new DataException("El número de identificación ya se encuentra registrado, pruebe con otro.", null);
                }
            }
        } else {
            user.setDni(userBase.get().getDni());
        }
        if (user.getEmail() != null && !user.getEmail().equals("")) {
            if (_userRep.existsByEmail(user.getEmail())) {
                if (!user.getEmail().equals(userBase.get().getEmail())) {
                    throw new DataException("El correo electrónico ya se encuentra registrado, pruebe con otro.", null);
                }
            }
        } else {
            user.setEmail(userBase.get().getEmail());
        }
        if (user.getUsername() != null && !user.getUsername().equals("")) {
            if (_userRep.existsByUsername(user.getUsername())) {
                if (!user.getUsername().equals(userBase.get().getUsername())) {
                    throw new DataException("El nombre de usuario ya se encuentra registrado, pruebe con otro.", null);
                }
            }
        } else {
            user.setUsername(userBase.get().getUsername());
        }
        if (user.getPassword() != null && !user.getPassword().equals("")) {
            throw new RuntimeException(
                    "La contraseña no puede ser editada de esta manera, visite el apartado de edición de contraseña.");
        } else {
            user.setPassword(userBase.get().getPassword());
        }
    }

    public UserResponseDto updateUser(User user) {
        validarPut(user);
        User updateUser = _userRep.save(user);
        return _mapper.mapObject(updateUser, UserResponseDto.class);
    }

    private void assignRole(User user, Long roleId) {
        try {
            Optional<Role> role = _roleRep.findById(roleId);
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(role.get());
            _userRoleRep.save(userRole);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Error al asignar el rol: %s", e.getMessage()));
        }
    }
}
