package app.services;

import app.model.Ingredient;
import app.model.Remains;
import app.repositories.RemainsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemainsService {
    @Autowired
    private RemainsRepository repository;

    public Remains addCoffee(int amount){
        Remains coffee = repository.findByIngredient(Ingredient.COFFEE);
        if (coffee == null) {
            coffee = new Remains();
            coffee.setIngredient(Ingredient.COFFEE);
            coffee.setRemains(amount);
            coffee.setReserved(0);
        } else {
            coffee.setRemains(coffee.getRemains() + amount);
        }
        return repository.saveAndFlush(coffee);
    }

    public Remains addMilk(int amount){
        Remains milk = repository.findByIngredient(Ingredient.MILK);
        if (milk == null) {
            milk = new Remains();
            milk.setIngredient(Ingredient.MILK);
            milk.setRemains(amount);
            milk.setReserved(0);
        } else {
            milk.setRemains(milk.getRemains() + amount);
        }
        return repository.saveAndFlush(milk);
    }

    public Remains addWater(int amount){
        Remains water = repository.findByIngredient(Ingredient.WATER);
        if (water == null) {
            water = new Remains();
            water.setIngredient(Ingredient.WATER);
            water.setRemains(amount);
            water.setReserved(0);
        } else {
            water.setRemains(water.getRemains() + amount);
        }
        return repository.saveAndFlush(water);
    }
}
