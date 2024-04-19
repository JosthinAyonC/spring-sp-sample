package sasf.base.security.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sasf.base.dto.JwtResponseDto;
import sasf.base.dto.LoginRequestDto;
import sasf.base.dto.UserRequestDto;
import sasf.base.dto.UserResponseDto;
import sasf.base.security.jwt.JwtUtils;
import sasf.base.security.securityServices.IUserDetails;
import sasf.base.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequestDto loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsernameoremail(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            IUserDetails userDetails = (IUserDetails) authentication.getPrincipal();

            return ResponseEntity.ok(new JwtResponseDto(
                    userDetails.getId(),
                    "Bearer " + jwt));

        } catch (Exception e) {
            throw new RuntimeException("Error de credenciales, por favor intente de nuevo.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRequestDto userRegister) {
        try {
            return ResponseEntity.ok(userService.createUser(userRegister));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo registrar al usuario: " + e.getMessage());
        }
    }
}