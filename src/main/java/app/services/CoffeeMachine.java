package app.services;

import javax.transaction.Transactional;

import app.model.Action;
import app.model.CupStatus;
import app.model.Ingredient;
import app.model.Remains;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import app.repositories.CupRepository;
import app.repositories.RecipeRepository;
import app.repositories.RemainsRepository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoffeeMachine {
    @Autowired
    private CupRepository cupRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RemainsRepository remainsRepository;

    @Scheduled(fixedDelay = 1000)
    public void checkQueue() {
        cupRepository.findAllByStatusOrderByDate(CupStatus.QUEUED).forEach(cup -> {
            if (cup.getRecipe() == null || cup.getRecipe().getActions() == null || cup.getRecipe().getActions().isEmpty()) {
                cup.setStatus(CupStatus.ERROR);
                cupRepository.saveAndFlush(cup);
            } else {
                cup.setStatus(CupStatus.PROCESSING);
                cup = cupRepository.saveAndFlush(cup);
                List<Action> actions = cup.getRecipe().getActions();
                Map<Ingredient, Integer> usedIngredients = actions.stream()
                        .flatMap(a -> a.getIngredients().stream())
                        .reduce(new HashMap<>(), (m, i) -> {
                            m.put(i.getIngredient(), m.getOrDefault(i.getIngredient(), 0) + i.getAmount());
                            return m;
                        }, (a, b) -> {
                            a.forEach((k, v) -> b.put(k, b.getOrDefault(k, 0) + v));
                            return b;
                        });
                if (usedIngredients.entrySet().stream().allMatch(e -> {
                    Remains r = remainsRepository.findByIngredient(e.getKey());
                    return r.getRemains() - e.getValue() >= 0 && r.getReserved() - e.getValue() >= 0;
                })) {
                    //здесь ещё нужно поставить чашку и указать её код
                    actions.stream().sorted(Comparator.comparingInt(Action::getCount)).forEachOrdered(action -> {
                        //имитируем физическую операцию
                        try {
                            Thread.sleep(30000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    updateRemains(usedIngredients);
                    //здесь чашка перемещается на стол ожидания
                    cup.setStatus(CupStatus.READY);
                } else {
                    cup.setStatus(CupStatus.ERROR);
                }
                cupRepository.saveAndFlush(cup);
            }
        });
    }

    @Transactional
    private void updateRemains(Map<Ingredient, Integer> usedIngredients){
        usedIngredients.forEach((k, v) -> {
            Remains r = remainsRepository.findByIngredient(k);
            r.setRemains(r.getRemains() - v);
            r.setReserved(r.getReserved() - v);
            remainsRepository.save(r);
        });
        remainsRepository.flush();
    }
}
