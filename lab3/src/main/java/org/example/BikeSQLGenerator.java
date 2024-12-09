package org.example;

/**
 * Спеціалізований SQL-генератор для сутності Bike
 */
public class BikeSQLGenerator extends SQLGenerator<Bike> {
    /**
     * Конструктор класу BikeSQLGenerator, ініціалізує його типом класу Bike
     */
    public BikeSQLGenerator() {
        super(Bike.class);
    }
}
