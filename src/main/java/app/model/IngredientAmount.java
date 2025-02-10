package app.model;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class IngredientAmount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Enumerated(EnumType.STRING)
    private Ingredient ingredient;
    private int amount;

    public IngredientAmount() {
    }

    public IngredientAmount(Ingredient ingredient, int amount){
        this.ingredient = ingredient;
        this.amount = amount;
    }
}
