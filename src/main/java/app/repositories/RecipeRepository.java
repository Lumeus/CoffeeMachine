package app.repositories;

import app.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe findByName(String name);

    boolean existsByName(String name);

//    @Query("SELECT r.recipe FROM " +
//            "(SELECT c.recipe, count(*) n " +
//            "FROM Cup c " +
//            "GROUP BY recipe " +
//            "ORDER BY n DESC " +
//            "LIMIT 1) r")
    @Query(value = "SELECT * FROM Recipe r " +
            "WHERE r.id = (SELECT recipe_id FROM Cup c " +
            "GROUP BY c.recipe_id " +
            "ORDER BY count(*) DESC " +
            "LIMIT 1)", nativeQuery = true)
    Recipe findMostPopular();
}
