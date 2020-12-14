package cz.uhk.pro2.todo.dao;

public class DBException extends RuntimeException {
    public DBException(String message, Throwable cause) {
        super(message, cause);
    }
}
