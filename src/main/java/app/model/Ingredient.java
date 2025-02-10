package app.model;

import lombok.Getter;

@Getter
public enum Ingredient {
    WATER("Вода", "мл"),
    MILK("Молоко", "мл"),
    COFFEE("Кофе", "г");

    private final String name;
    private final String unit;

    Ingredient(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }

}
