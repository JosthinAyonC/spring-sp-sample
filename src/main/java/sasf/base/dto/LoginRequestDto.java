package sasf.base.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginRequestDto {
    @NotBlank(message = "El 'username' o 'email' es obligatorio.")
    private String usernameoremail;

    @NotBlank(message = "La 'password' es obligatoria.")
    private String password;
}
