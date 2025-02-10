package app.model;

import javax.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int count;
    @Enumerated(EnumType.STRING)
    private ActionType type;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "action_id")
    private List<IngredientAmount> ingredients;

    public Action(){}

    private Action(int count, ActionType type, List<IngredientAmount> ingredients){
        this.type = type;
        this.ingredients = new ArrayList<>();
        this.ingredients.addAll(ingredients);
        this.count = count;
    }

    public static Action coffee(int order, int coffee, int water){
        return new Action(order, ActionType.COFFEE, Arrays.asList(
                new IngredientAmount(Ingredient.COFFEE, coffee),
                new IngredientAmount(Ingredient.WATER, water)
        ));
    }

    public static Action water(int order, int amount){
        return new Action(order, ActionType.WATER, Arrays.asList(
                new IngredientAmount(Ingredient.WATER, amount)
        ));
    }

    public static Action milk(int order, int amount){
        return new Action(order, ActionType.MILK, Arrays.asList(
                new IngredientAmount(Ingredient.MILK, amount)
        ));
    }

    public static Action milkFoam(int order, int amount){
        return new Action(order, ActionType.FOAM, Arrays.asList(
                new IngredientAmount(Ingredient.MILK, amount)
        ));
    }
}
