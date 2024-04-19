package sasf.base.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;

    private String username;

    private String email;

    private String dni;

    private String firstname;

    private String lastname;

    private Boolean status;

    @JsonProperty("roles")
    private Set<String> roles = new HashSet<>();

}
