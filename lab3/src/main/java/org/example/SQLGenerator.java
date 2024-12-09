package org.example;

import java.lang.reflect.Field;

/**
 * Утиліта для створення SQL-запитів (INSERT, SELECT, UPDATE, DELETE)
 */
public abstract class SQLGenerator<T> {
    private final Class<T> clas;

    /**
     * Конструктор SQLGenerator для певного класу
     * @param clas
     */
    public SQLGenerator(Class<T> clas) {
        this.clas = clas;
    }

    /**
     * Генерує INSERT запит для заданого об'єкта
     *
     * @param object об'єкт, для якого потрібно згенерувати запит
     * @return рядок String, що містить згенерований INSERT запит
     * @throws IllegalAccessException у випадку невдалого доступу до поля
     */
    public String generateInsertQuery(T object) throws IllegalAccessException {
        checkTableAnnotation();

        Table table = clas.getAnnotation(Table.class);
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for (Field field : clas.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                columns.append(column.name()).append(", ");
                field.setAccessible(true);
                values.append("'").append(field.get(object)).append("', ");
            }
        }

        trimTrailingComma(columns);
        trimTrailingComma(values);

        return String.format("INSERT INTO %s (%s) VALUES (%s);", table.name(), columns, values);
    }

    /**
     * Генерує SELECT запит для заданого класу
     *
     * @return рядок String, що містить згенерований SELECT запит
     */
    public String generateSelectQuery() {
        checkTableAnnotation();

        Table table = clas.getAnnotation(Table.class);
        return String.format("SELECT * FROM %s;", table.name());
    }

    /**
     * Генерує UPDATE запит для заданого об'єкта
     *
     * @param object об'єкт, для якого потрібно згенерувати запит
     * @return рядок String, що містить згенерований UPDATE запит
     * @throws IllegalAccessException у випадку невдалого доступу до поля
     */
    public String generateUpdateQuery(T object) throws IllegalAccessException {
        checkTableAnnotation();

        Table table = clas.getAnnotation(Table.class);
        StringBuilder setClause = new StringBuilder();
        String primaryKeyCondition = "";

        for (Field field : clas.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                field.setAccessible(true);

                if (column.primaryKey()) {
                    primaryKeyCondition = String.format("%s = '%s'", column.name(), field.get(object));
                } else {
                    setClause.append(String.format("%s = '%s', ", column.name(), field.get(object)));
                }
            }
        }

        trimTrailingComma(setClause);

        if (primaryKeyCondition.isEmpty()) {
            throw new IllegalArgumentException("No primary key found for update query.");
        }

        return String.format("UPDATE %s SET %s WHERE %s;", table.name(), setClause, primaryKeyCondition);
    }

    /**
     * Генерує DELETE запит для заданого об'єкта
     *
     * @param object об'єкт, для якого потрібно згенерувати запит
     * @return рядок String, що містить згенерований DELETE запит
     * @throws IllegalAccessException у випадку невдалого доступу до поля
     */
    public String generateDeleteQuery(T object) throws IllegalAccessException {
        checkTableAnnotation();

        Table table = clas.getAnnotation(Table.class);
        String primaryKeyCondition = "";

        for (Field field : clas.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                if (column.primaryKey()) {
                    field.setAccessible(true);
                    primaryKeyCondition = String.format("%s = '%s'", column.name(), field.get(object));
                    break;
                }
            }
        }

        if (primaryKeyCondition.isEmpty()) {
            throw new IllegalArgumentException("No primary key found for delete query.");
        }

        return String.format("DELETE FROM %s WHERE %s;", table.name(), primaryKeyCondition);
    }

    /**
     * Перевіряє, чи заданий клас має анотацію @Table
     *
     * @throws IllegalArgumentException у випадку якщо клас не анотовано з @Table
     */
    private void checkTableAnnotation() {
        if (!clas.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Class must be annotated with @Table");
        }
    }

    /**
     * Прибирає останню кому та пробіл
     * @param builder змінний рядок
     */
    private static void trimTrailingComma(StringBuilder builder) {
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 2);
        }
    }
}
