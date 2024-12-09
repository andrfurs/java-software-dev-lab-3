package org.example;

/**
 * Спеціалізований SQL-генератор для сутності Plane
 */
public class PlaneSQLGenerator extends SQLGenerator<Plane> {
    /**
     * Конструктор класу PlaneSQLGenerator, ініціалізує його типом класу Plane
     */
    public PlaneSQLGenerator() {
        super(Plane.class);
    }
}
