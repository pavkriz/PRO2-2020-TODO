package cz.uhk.pro2.todo.dao;

import cz.uhk.pro2.todo.model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    public void save(Task task) {
        // TODO DU 24.11., pouzivejte parametry v prepared statementu ( ?, ?)
        try (Connection con = openConnection()) {

            PreparedStatement p;
            if (task.getId() == 0) {
                // TODO INSERT
                p = con.prepareStatement("INSERT INTO task (id, description, due_date, done) VALUES " +
                        "(DEFAULT," + task.getDescription() + "," + task.getDueDate() + "," + task.isDone() + ")");
                p.executeQuery();
            } else {
                // TODO UPDATE
                p = con.prepareStatement("UPDATE TABLE task SET Description=" + task.getDescription() +
                        ", due_date="+ task.getDueDate() +
                        ", done=" + task.isDone() +
                        " WHERE id=" + task.getId());
                p.executeQuery();
            }

        } catch (SQLException throwables) {
            throw new DbException("Error during DB operation", throwables); // obalime puvodni vyjimku nasi runtime vyjimkou
        }
    }

    public void delete(Task task) {
        delete(task.getId());
    }

    public void delete(long id) {
        // TODO DU 24.11.
        try (Connection con = openConnection()) {
            PreparedStatement p = con.prepareStatement("DELETE FROM test where id=" + id);
            p.executeQuery();

        } catch (SQLException throwables) {
            throw new DbException("Error during DB operation", throwables); // obalime puvodni vyjimku nasi runtime vyjimkou
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
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=UTC","root", "Vojta131Vojta131");
    }
}
