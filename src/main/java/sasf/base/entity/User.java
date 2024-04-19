package sasf.base.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username", name = "unique_username"),
        @UniqueConstraint(columnNames = "email", name = "unique_email"),
        @UniqueConstraint(columnNames = "dni", name = "unique_dni")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "El 'id' no puede ser nulo.")
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String username;

    @Column(length = 18, nullable = false, unique = true)
    private String dni;

    @Column(length = 50, nullable = false, unique = true)
    private String email;

    @Column(length = 75, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String firstname;

    @Column(length = 50, nullable = false)
    private String lastname;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    private Boolean status = true;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties({ "user" })
    @JsonProperty("roles")
    private Set<UserRole> userRoles = new HashSet<>();

    public Set<Role> getUserRoles() {
        return userRoles.stream().map(UserRole::getRole).collect(java.util.stream.Collectors.toSet());
    }
}
