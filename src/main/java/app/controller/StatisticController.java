package app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import app.services.StatisticService;

import java.util.Optional;

@RestController
@RequestMapping(value = "statistic")
@Tag(name = "StatisticController", description = "Здесь можно узнать самый популярный рецепт и удалить старую статистику")
public class StatisticController {
    @Autowired
    StatisticService service;

    @RequestMapping(value = "most-popular", method = RequestMethod.GET)
    @Operation(summary = "Самый популярный рецепт", description = "Позволяет получить имя самого популярного рецепта основываясь на хранящейся в базе истории")
    public ResponseEntity<String> getMostPopular(){
        return ResponseEntity.of(Optional.ofNullable(service.getMostPopular()));
    }

    @RequestMapping(value = "clean-old", method = RequestMethod.GET)
    @Operation(summary = "Удалить старую статистику", description = "Удаляет из базы историю старше указанного числа дней")
    public ResponseEntity<Object> getMostPopular(@RequestParam(value = "days", defaultValue = "-1") int days){
        if (days < 0) service.cleanOldStatistic();
        else service.cleanOldStatistic(days);
        return ResponseEntity.ok().build();
    }
}
