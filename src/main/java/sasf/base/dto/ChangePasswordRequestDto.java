package sasf.base.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequestDto {
    @NotNull(message = "El 'id' no puede ser nulo.")
    private Long id;
    private String oldPassword;
    @Size(min = 8, message = "La 'password' debe tener al menos 8 caracteres.")
    @Pattern(regexp = ".*[A-Z].*", message = "La 'password' debe contener al menos una letra mayúscula.")
    @Pattern(regexp = "^(?=.*[0-9]).+$", message = "La 'password' debe contener al menos un caracter numérico.")
    @Pattern(regexp = ".*[@#$%^&+=].*", message = "La 'password' debe contener al menos un carácter especial.")
    private String newPassword;
}
