package cz.uhk.pro2.todo.dao;

import cz.uhk.pro2.todo.model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    PreparedStatement statement;
    Connection connection;

    public void save(Task task) {
        boolean inserted = false;
        try {
            connection = openConnection();
            // TODO DU 24.11., pouzivejte parametry v prepared statementu ( ?, ?)
            if (task.getId() == 0) {
                statement = connection.prepareStatement("insert into task (description, due_date, done) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                inserted = true;
            } else {
                statement = connection.prepareStatement("update task set description=?, due_date=?, done=? where id=?", Statement.RETURN_GENERATED_KEYS);
                statement.setLong(4, task.getId());
            }
            statement.setString(1, task.getDescription());
            statement.setDate(2, new Date(task.getDueDate().getTime()));
            statement.setBoolean(3, task.isDone());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next() && inserted) {
                task.setId(rs.getLong(1));
            }
            connection.close();
        } catch (SQLException e) {
            throw new DbException("SQL update/insert", e);
        }
    }


    public void delete(Task task) {
        delete(task.getId());
    }

    public void delete(long id) {
        try {
            connection = openConnection();
            if (id != 0) {
                statement = connection.prepareStatement("DELETE FROM task WHERE id=?");
            } else {
                return;
            }
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new DbException("SQL delete error", e);
        }
    }

    public List<Task> findAll() {
        try (Connection con = openConnection()) {
            PreparedStatement s = con.prepareStatement("SELECT id, description, due_date, done FROM task ORDER BY id");
            ResultSet rs = s.executeQuery();
            List<Task> tasks = new ArrayList<>();
            while (rs.next()) {
                // zpracovavame jeden radek z vysledku dotazu
                Task t = new Task(rs.getLong("id"), rs.getString("description"), rs.getDate("due_date"), rs.getBoolean("done"));
                tasks.add(t);
            }
            return tasks;
        } catch (SQLException throwables) {
            throw new DbException("Error during DB operation", throwables); // obalime puvodni vyjimku nasi runtime vyjimkou
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/dbpro2", "dbpro2", "password");
        //return DriverManager.getConnection("jdbc:mysql://localhost:3306/dbpro2","root", "my-secret-pw");
    }

    public void deleteAll() {
        try {
            connection = openConnection();
            statement = connection.prepareStatement("DELETE FROM task");
            statement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            throw new DbException("SQL delete error", e);
        }
    }
}
