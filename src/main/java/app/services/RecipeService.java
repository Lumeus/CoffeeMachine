package app.services;

import app.model.CupStatus;
import app.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.repositories.CupRepository;
import app.repositories.RecipeRepository;
import app.repositories.RemainsRepository;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private CupRepository cupRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RemainsRepository remainsRepository;
    private final List<String> systemRecipes = Arrays.asList("Эспрессо", "Американо", "Капучино");

    public Recipe saveNewRecipe(Recipe recipe){
        if (recipe == null || systemRecipes.contains(recipe.getName()) || recipeRepository.existsByName(recipe.getName())) {
            return null;
        } else {
            return recipeRepository.saveAndFlush(recipe);
        }
    }

    @Transactional
    public boolean deleteRecipe(String name){
        if (name != null && !systemRecipes.contains(name) && recipeRepository.existsByName(name)) {
            Recipe recipe = recipeRepository.findByName(name);
            if (!cupRepository.existsByRecipeAndStatus(recipe, CupStatus.QUEUED) && !cupRepository.existsByRecipeAndStatus(recipe, CupStatus.PROCESSING)) {
                cupRepository.deleteAll(cupRepository.findAllByRecipe(recipe));
                recipeRepository.delete(recipe);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
