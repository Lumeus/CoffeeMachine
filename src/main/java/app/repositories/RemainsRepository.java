package app.repositories;

import app.model.Ingredient;
import app.model.Remains;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RemainsRepository extends JpaRepository<Remains, Long> {
    Remains findByIngredient(Ingredient ingredient);
}
