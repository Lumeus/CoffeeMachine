package app.services;

import app.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import app.repositories.CupRepository;
import app.repositories.RecipeRepository;

import java.util.Calendar;

@Service
public class StatisticService {
    @Autowired
    private CupRepository cupRepository;
    @Autowired
    private RecipeRepository recipeRepository;

    public String getMostPopular() {
        Recipe recipe = recipeRepository.findMostPopular();
        if (recipe == null) return null;
        return recipe.getName();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanOldStatistic() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -5);
        cupRepository.deleteAll(cupRepository.findAllOlder(calendar.getTime()));
    }

    public void cleanOldStatistic(int days){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, -days);
        cupRepository.deleteAll(cupRepository.findAllOlder(calendar.getTime()));
    }
}
