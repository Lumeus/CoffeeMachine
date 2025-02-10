package app.model;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
public class Remains {
    @Id
    @Enumerated(EnumType.STRING)
    private Ingredient ingredient;
    private int remains;
    private int reserved;
}
