package app.controller;

import app.model.Cup;
import app.model.Recipe;
import app.model.Remains;
import app.services.RemainsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import app.services.Planner;
import app.services.RecipeService;
import app.services.StatisticService;

import java.util.Optional;

@RestController
@RequestMapping(value = "coffee")
@Tag(name = "CoffeeController", description = "Операции с рецептами и заказ кофе")
public class CoffeeController {
    @Autowired
    private Planner planner;
    @Autowired
    private StatisticService statisticService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private RemainsService remainsService;


    @RequestMapping(value = "most-popular", method = RequestMethod.GET)
    @Operation(summary = "Самый популярный напиток", description = "Позволяет заказать самый популярный напиток (см. StatisticController)")
    public ResponseEntity<Cup> getMostPopular(@RequestParam(value = "name", defaultValue = "cup") String name){
        return ResponseEntity.of(Optional.ofNullable(planner.queueCup(statisticService.getMostPopular(), name)));
    }
    @RequestMapping(value = "espresso", method = RequestMethod.GET)
    @Operation(summary = "Эспрессо", description = "Позволяет заказать классический эспрессо")
    public ResponseEntity<Cup> getEspresso(@RequestParam(value = "name", defaultValue = "cup") String name){
        return ResponseEntity.of(Optional.ofNullable(planner.queueCupEspresso(name)));
    }
    @RequestMapping(value = "americano", method = RequestMethod.GET)
    @Operation(summary = "Американо", description = "Позволяет заказать классический американо")
    public ResponseEntity<Cup> getAmericano(@RequestParam(value = "name", defaultValue = "cup") String name){
        return ResponseEntity.of(Optional.ofNullable(planner.queueCupAmericano(name)));
    }
    @RequestMapping(value = "cappuccino", method = RequestMethod.GET)
    @Operation(summary = "Капучино", description = "Позволяет заказать классический капучино")
    public ResponseEntity<Cup> getCappuccino(@RequestParam(value = "name", defaultValue = "cup") String name){
        return ResponseEntity.of(Optional.ofNullable(planner.queueCupCappuccino(name)));
    }
    @RequestMapping(value = "recipe/{recipe}", method = RequestMethod.GET)
    @Operation(summary = "Кофе по рецепту", description = "Позволяет заказать кофе по предопределенному рецепту")
    public ResponseEntity<Cup> getByRecipe(@PathVariable(value = "recipe") String recipe, @RequestParam(value = "name", defaultValue = "cup") String name){
        return ResponseEntity.of(Optional.ofNullable(planner.queueCup(recipe, name)));
    }
    @RequestMapping(value = "recipe", method = RequestMethod.POST)
    @Operation(summary = "Новый рецепт", description = "Позволяет определить новый рецепт")
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe){
        return ResponseEntity.of(Optional.ofNullable(recipeService.saveNewRecipe(recipe)));
    }
    @RequestMapping(value = "recipe/{recipe}", method = RequestMethod.DELETE)
    @Operation(summary = "Удалить рецепт", description = "Позволяет удалить рецепт")
    public ResponseEntity<Object> deleteRecipe(@PathVariable(value = "recipe") String recipe){
        if (recipeService.deleteRecipe(recipe)) return ResponseEntity.ok().build();
        else return ResponseEntity.notFound().build();
    }
    @RequestMapping(value = "cup", method = RequestMethod.GET)
    @Operation(summary = "Чашка", description = "Позволяет узнать статус заказанной чашки кофе")
    public ResponseEntity<Cup> getCup(@RequestParam(value = "name", defaultValue = "cup") String name, @RequestParam(value = "count") int count){
        return ResponseEntity.of(Optional.ofNullable(planner.getCup(name, count)));
    }
    @RequestMapping(value = "cup", method = RequestMethod.DELETE)
    @Operation(summary = "Удалить чашку", description = "Позволяет удалить чашку, если она всё ещё в очереди")
    public ResponseEntity<Object> cancelCup(@RequestParam(value = "name", defaultValue = "cup") String name, @RequestParam(value = "count") int count){
        if (planner.cancelCup(name, count)) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
    }
    @RequestMapping(value = "addCoffee", method = RequestMethod.POST)
    @Operation(summary = "Добавить кофе", description = "Позволяет пополнить запас кофе")
    public ResponseEntity<Remains> addCoffee(@RequestBody int amount){
        if (amount < 0) return ResponseEntity.badRequest().build();
        else return ResponseEntity.of(Optional.ofNullable(remainsService.addCoffee(amount)));
    }
    @RequestMapping(value = "addMilk", method = RequestMethod.POST)
    @Operation(summary = "Добавить молоко", description = "Позволяет пополнить запас молока")
    public ResponseEntity<Remains> addMilk(@RequestBody int amount){
        if (amount < 0) return ResponseEntity.badRequest().build();
        else return ResponseEntity.of(Optional.ofNullable(remainsService.addMilk(amount)));
    }
    @RequestMapping(value = "addWater", method = RequestMethod.POST)
    @Operation(summary = "Добавить воду", description = "Позволяет пополнить запас воды")
    public ResponseEntity<Remains> addWater(@RequestBody int amount){
        if (amount < 0) return ResponseEntity.badRequest().build();
        else return ResponseEntity.of(Optional.ofNullable(remainsService.addWater(amount)));
    }
}
