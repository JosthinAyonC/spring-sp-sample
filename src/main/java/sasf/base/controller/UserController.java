package sasf.base.controller;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import sasf.base.dto.ChangePasswordRequestDto;
import sasf.base.dto.UserRequestDto;
import sasf.base.dto.UserResponseDto;
import sasf.base.entity.User;
import sasf.base.security.securityServices.IUserDetails;
import sasf.base.service.UserService;
import sasf.base.utils.Globales;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService _userService;

    @Operation(summary = "Listar todos los usuarios")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MOD')")
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> listAllUsers(
            @PageableDefault(page = Globales.PAGEABLE_PAGE_DEFAULT, size = Globales.PAGEABLE_SIZE_DEFAULT) Pageable pageable) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(_userService.listAllUsers(pageable));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo listar los usuarios: " + e.getMessage());
        }
    }

    @Operation(summary = "Lista usuario por su id")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MOD')")
    @GetMapping("{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(_userService.getUserById(id));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener al usuario: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MOD')")
    @GetMapping("dni/{dni}")
    public ResponseEntity<UserResponseDto> getUserByDi(@PathVariable String dni) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(_userService.getUserByDni(dni));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener al usuario: " + e.getMessage());
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('MOD')")
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto user) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(_userService.createUser(user));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear al usuario: " + e.getMessage());
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("mod")
    public ResponseEntity<UserResponseDto> createMod(@RequestBody @Valid UserRequestDto user) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(_userService.createMod(user));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo crear al moderador: " + e.getMessage());
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or hasRole('MOD')")
    @PutMapping("update")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody User user) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(_userService.updateUser(user));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar al usuario: " + e.getMessage());
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("delete")
    public ResponseEntity<String> deleteUser(@RequestBody @Valid User user) {
        try {
            user.setStatus(false);
            _userService.updateUser(user);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body("{\"msg\":\"Se eliminó al usuario satisfactoriamente\"}");
        } catch (Exception e) {
            throw new RuntimeException("No se pudo eliminar al usuario: " + e.getMessage());
        }
    }

    @Transactional
    @PutMapping("change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequestDto changePassword) {
        try {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(_userService.changePassword(changePassword));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo actualizar la contraseña: " + e.getMessage());
        }
    }

    @GetMapping("/myUser")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Long userId = ((IUserDetails) authentication.getPrincipal()).getId();
            UserResponseDto userDetails = _userService.getUserById(userId);
            return ResponseEntity.status(HttpStatus.OK).body(userDetails);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener la información del usuario logeado: " + e.getMessage());
        }
    }
}
