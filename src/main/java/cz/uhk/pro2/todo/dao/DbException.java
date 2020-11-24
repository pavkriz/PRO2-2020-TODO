package cz.uhk.pro2.todo.dao;

public class DbException extends RuntimeException {

    public DbException(String message, Throwable cause) {
        super(message, cause);
    }
}
