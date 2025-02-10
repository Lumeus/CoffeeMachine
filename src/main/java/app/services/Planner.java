package app.services;

import javax.transaction.Transactional;

import app.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.repositories.CupRepository;
import app.repositories.RecipeRepository;
import app.repositories.RemainsRepository;

import java.util.*;

@Service
public class Planner {
    @Autowired
    private CupRepository cupRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RemainsRepository remainsRepository;

    public Cup queueCup(String recipe, String name){
        Recipe r = recipeRepository.findByName(recipe);
        if (r == null) return null;
        return queueCup(r, name);
    }
    public Cup queueCupEspresso(String name){
        Recipe r = recipeRepository.findByName("Эспрессо");
        if (r == null) {
            r = new Recipe();
            r.setName("Эспрессо");
            r.setActions(Arrays.asList(Action.coffee(1,7,30)));
            recipeRepository.saveAndFlush(r);
        }
        return queueCup(r, name);
    }
    public Cup queueCupAmericano(String name){
        Recipe r = recipeRepository.findByName("Американо");
        if (r == null) {
            r = new Recipe();
            r.setName("Американо");
            r.setActions(Arrays.asList(Action.coffee(1,14,60), Action.water(2,140)));
            recipeRepository.saveAndFlush(r);
        }
        return queueCup(r, name);
    }
    public Cup queueCupCappuccino(String name){
        Recipe r = recipeRepository.findByName("Капучино");
        if (r == null) {
            r = new Recipe();
            r.setName("Капучино");
            r.setActions(Arrays.asList(
                    Action.coffee(1,14,60),
                    Action.milk(2,60),
                    Action.milkFoam(3,60)
            ));
            recipeRepository.saveAndFlush(r);
        }
        return queueCup(r, name);
    }

    private Cup queueCup(Recipe recipe, String name){
        if (reserveIngredients(recipe)) {
            Cup cup = new Cup();
            cup.setDate(new Date());
            cup.setName(name);
            cup.setRecipe(recipe);
            cup.setCount(Optional.ofNullable(cupRepository.findMaxCountByName(name)).orElse(0) + 1);
            cup.setStatus(CupStatus.QUEUED);
            return cupRepository.saveAndFlush(cup);
        } else {
            return null;
        }
    }

    public boolean cancelCup(String name, int count){
        Cup cup = cupRepository.findByNameAndCount(name, count);
        if (cup != null && cup.getStatus() == CupStatus.QUEUED) {
            cupRepository.delete(cup);
            return true;
        } else return false;
    }

    public Cup getCup(String name, int count){
        return cupRepository.findByNameAndCount(name, count);
    }

    @Transactional
    private boolean reserveIngredients(Recipe recipe){
        List<Action> actions = recipe.getActions();
        Set<Map.Entry<Ingredient, Integer>> usedIngredients = actions.stream()
                .flatMap(a -> a.getIngredients().stream())
                .reduce(new HashMap<Ingredient, Integer>(), (m, i) -> {
                    m.put(i.getIngredient(), m.getOrDefault(i.getIngredient(), 0) + i.getAmount());
                    return m;
                }, (a, b) -> {
                    a.forEach((k, v) -> b.put(k, b.getOrDefault(k, 0) + v));
                    return b;
                }).entrySet();
        Map<Ingredient, Remains> remains = new HashMap<>();
        if (usedIngredients.stream().allMatch(e -> {
            Remains r = remainsRepository.findByIngredient(e.getKey());
            remains.put(e.getKey(), r);
            return r.getRemains() - e.getValue() >= 0;
        })) {
            usedIngredients.forEach(e -> {
                Remains r = remains.get(e.getKey());
                r.setReserved(r.getReserved() + e.getValue());
                remainsRepository.save(r);
            });
            remainsRepository.flush();
            return true;
        } else {
            return false;
        }
    }
}
