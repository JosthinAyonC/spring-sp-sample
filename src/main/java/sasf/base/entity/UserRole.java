package sasf.base.entity;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "userrole")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idUser")
    @JsonIgnoreProperties({"user"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "idRole")
    private Role role;

    @CreationTimestamp
    private Date createdAt;
    
    @UpdateTimestamp
    private Date updatedAt;
}