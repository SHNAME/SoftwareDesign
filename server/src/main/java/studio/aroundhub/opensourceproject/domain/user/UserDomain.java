package studio.aroundhub.opensourceproject.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",updatable = false)
    private long id;

    @Column(name = "email",updatable = false,nullable = false)
    private String email;

    @Column(name = "password",updatable = false,nullable = false)
    private String password;

    @Column(name = "name",updatable = false,nullable = false)
    private String name;


    @Builder
    public UserDomain(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }



}
