package cz.uhk.pro2.todo.database;

public class DbException extends RuntimeException {
    public DbException(String message, Throwable cause) {
        super(message, cause);
    }
}