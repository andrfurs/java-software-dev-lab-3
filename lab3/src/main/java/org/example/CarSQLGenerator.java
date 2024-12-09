package org.example;

/**
 * Спеціалізований SQL-генератор для сутності Car
 */
public class CarSQLGenerator extends SQLGenerator<Car> {
    /**
     * Конструктор класу CarSQLGenerator, ініціалізує його типом класу Car
     */
    public CarSQLGenerator() {
        super(Car.class);
    }
}
