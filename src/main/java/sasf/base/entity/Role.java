package sasf.base.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name", name = "unique_name"),
})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El 'name' es obligatorio.")
    @Column(length = 25, nullable = false, unique = true)
    private String name;

    private String description;

    @JsonIgnore
    @CreationTimestamp
    private Date createdAt;

    @JsonIgnore
    @UpdateTimestamp
    private Date updatedAt;

    @JsonIgnore
    private boolean status = true;

}