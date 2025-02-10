package app.model;

import javax.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Recipe {
//    public static final Recipe ESPRESSO = new Recipe("Эспрессо", Arrays.asList(Action.coffee(1,7,30)));
//    public static final Recipe AMERICANO = new Recipe("Американо", Arrays.asList(Action.coffee(1,14,60), Action.water(2,140)));
//    public static final Recipe CAPPUCCINO = new Recipe("Капучино", Arrays.asList(Action.coffee(1,14,60), Action.milk(2,60), Action.milkFoam(3,60)));

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id")
    private List<Action> actions;
//    private boolean deletable;

//    public Recipe(String name, List<Action> actions) {
//        this.name = name;
//        this.actions = new ArrayList<>();
//        this.actions.addAll(actions);
//        this.deletable = true;
//    }
}
