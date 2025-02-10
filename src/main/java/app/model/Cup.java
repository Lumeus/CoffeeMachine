package app.model;

import javax.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"date", "name", "count"}))
public class Cup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private CupStatus status;
    private Date date;
    private String name;
    private int count;
    @ManyToOne
    private Recipe recipe;
}
