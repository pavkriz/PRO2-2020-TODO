package cz.uhk.pro2.todo.dao;

import cz.uhk.pro2.todo.model.Task;

import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    public void save(Task task) {
        try(Connection con = openConnection()) {
            PreparedStatement s = con.prepareStatement("SELECT id FROM task WHERE id=?");
            s.setInt(1, task.getId());
            ResultSet rs = s.executeQuery();
            if (!rs.next()) {
                PreparedStatement statement = con.prepareStatement("INSERT INTO task (description, due_date, done) VALUES (?,?,?)");
                statement.setString(1, task.getDescription());
                statement.setTimestamp(2, new Timestamp(task.getDueDate().getTime()));
                statement.setBoolean(3, task.isDone());
                statement.execute();
            } else {
                PreparedStatement statement = con.prepareStatement("UPDATE task SET description=?, due_date=?, done=? WHERE id=?");
                statement.setString(1, task.getDescription());
                statement.setTimestamp(2, new Timestamp(task.getDueDate().getTime()));
                statement.setBoolean(3, task.isDone());
                statement.setInt(4, task.getId());
                statement.execute();
            }
        } catch (SQLException throwables) {
            throw new DbException("Error during DB operation", throwables);
        }
    }

    public void delete(Task task) {
        delete(task.getId());
    }

    public void delete(int id) {
        try(Connection con = openConnection()) {
            PreparedStatement statement = con.prepareStatement("DELETE from task WHERE id=?");
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException throwables) {
            throw new DbException("Error during DB operation", throwables);
        }
    }

    public List<Task> findAll() {
        try (Connection con = openConnection()) {
            PreparedStatement s = con.prepareStatement("SELECT id, description, due_date, done FROM task ORDER BY id");
            ResultSet rs = s.executeQuery();
            List<Task> tasks = new ArrayList<>();
            while (rs.next()) {
                // zpracovavame jeden radek z vysledku dotazu
                Task t = new Task(rs.getInt("id"), rs.getString("description"), rs.getTimestamp("due_date"), rs.getBoolean("done"));
                tasks.add(t);
            }
            return tasks;
        } catch (SQLException throwables) {
            throw new DbException("Error during DB operation", throwables); // obalime puvodni vyjimku nasi runtime vyjimkou
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb","root", "my-secret-pw");
    }
}
