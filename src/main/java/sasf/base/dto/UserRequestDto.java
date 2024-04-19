package sasf.base.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private Long id;
    
    @NotBlank(message = "El 'username' es obligatorio.")
    @Length(min = 4, max = 20, message = "El 'username' debe tener entre 4 y 20 caracteres.")
    private String username;

    @Email(message = "El 'email' debe tener un formato correcto.")
    @NotBlank(message = "El 'email' es obligatorio.")
    @Length(min = 4, max = 50, message = "El 'email' debe tener entre 4 y 50 caracteres.")
    private String email;

    @NotBlank(message = "El 'dni' es obligatorio.")
    @Pattern(regexp = "^[0-9]+$", message = "El 'dni' debe contener solo números.")
    @Length(min = 9, max = 18, message = "El 'dni' debe tener entre 10 y 18 caracteres.")
    private String dni;

    @NotBlank(message = "La 'password' es obligatoria.")
    @Size(min = 8, message = "La 'password' debe tener al menos 8 caracteres.")
    @Pattern(regexp = "^(?=.*[0-9]).+$", message = "La 'password' debe contener al menos un caracter numérico.")
    @Pattern(regexp = ".*[A-Z].*", message = "La 'password' debe contener al menos una letra mayúscula.")
    @Pattern(regexp = ".*[@#$%^&+=].*", message = "La 'password' debe contener al menos un carácter especial.")
    @Length(min = 8, max = 75, message = "La 'password' debe tener entre 8 y 75 caracteres.")
    private String password;

    @NotBlank(message = "El 'firstname' es obligatorio.")
    @Length(min = 4, max = 50, message = "El 'firstname' debe tener entre 4 y 50 caracteres.")
    private String firstname;

    @NotBlank(message = "El 'lastname' es obligatorio.")
    @Length(min = 4, max = 50, message = "El 'lastname' debe tener entre 4 y 50 caracteres.")
    private String lastname;

    private Long RoleId;

}
