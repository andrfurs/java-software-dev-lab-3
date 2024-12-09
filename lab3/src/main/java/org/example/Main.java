package org.example;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        Bike bike = new Bike(1, "BMW", 45, 210, 450);
        Car car = new Car(1, "Mercedes", 350, 1800, 250);
        Plane plane = new Plane(1, "Boeing", "passenger", 3500);

        BikeSQLGenerator bikeSQLGenerator = new BikeSQLGenerator();
        CarSQLGenerator carSQLGenerator = new CarSQLGenerator();
        PlaneSQLGenerator planeSQLGenerator = new PlaneSQLGenerator();

        System.out.println(bikeSQLGenerator.generateInsertQuery(bike));
        System.out.println(bikeSQLGenerator.generateSelectQuery());
        System.out.println(carSQLGenerator.generateDeleteQuery(car));
        System.out.println(planeSQLGenerator.generateUpdateQuery(plane));
    }
}