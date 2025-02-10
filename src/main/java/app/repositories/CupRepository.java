package app.repositories;

import app.model.Cup;
import app.model.CupStatus;
import app.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CupRepository extends JpaRepository<Cup, Long> {
    @Query("from Cup c where c.date >= CURRENT_DATE and c.name = :name and c.count = :count")
    Cup findByNameAndCount(String name, int count);

    @Query("select MAX(c.count) from Cup c where c.date >= CURRENT_DATE and c.name = :name")
    Integer findMaxCountByName(String name);

    Cup findFirstByStatusOrderByDate(CupStatus status);

    List<Cup> findAllByStatusOrderByDate(CupStatus status);

    @Query("from Cup c where c.date < :date")
    List<Cup> findAllOlder(Date date);

    boolean existsByRecipeAndStatus(Recipe recipe, CupStatus status);

    List<Cup> findAllByRecipe(Recipe recipe);
}
