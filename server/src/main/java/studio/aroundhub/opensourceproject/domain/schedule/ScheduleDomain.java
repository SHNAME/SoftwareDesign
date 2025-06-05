package studio.aroundhub.opensourceproject.domain.schedule;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="scheduledomain")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ScheduleDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "email", updatable = false, nullable = false)
    private String email;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "schedule", nullable = false)
    private LocalDate schedule;




    @Builder
    public ScheduleDomain(String email, String title, String category, LocalDate schedule) {
        this.email = email;
        this.title = title;
        this.category = category;
        this.schedule = schedule;

    }

    @Override
    public String toString() {
        return "ScheduleDomain [id=" + id + ", email=" + email + ", title=" + title + ", category=" + category + "]";
    }
}
