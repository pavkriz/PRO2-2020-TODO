package cz.uhk.pro2.todo.database;

import cz.uhk.pro2.todo.model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    public void save(Task task) {
        try (Connection con = openConnection()) {
            PreparedStatement s = con.prepareStatement("INSERT INTO tasks (id, description, due_date, done) VALUES (?, ?, ?, ?)");

            if (task.getId() == 0) {
                //INSERT
                s.setInt(1, (int) task.getId());
            } else {
                // UPDATE
                s = con.prepareStatement("UPDATE tasks SET description=?, due_date=?, done=? where id=?");
                s.setLong(4, task.getId());
            }

            s.setString(2, task.getDescription());
            s.setDate(3, new Date(task.getDate().getTime()));
            s.setBoolean(4, task.isDone());
            s.executeUpdate();
        } catch (SQLException throwables) {
            throw new DbException("Error during DB operation", throwables); // obalime puvodni vyjimku nasi runtime vyjimkou
        }
    }

    public void delete(Task task) {
        delete(task.getId());
    }

    public void delete(long id) {
        try (Connection con = openConnection()) {
            PreparedStatement s = con.prepareStatement("DELETE FROM tasks WHERE id=?;");
            s.setString(1, String.valueOf(id));
            s.executeUpdate();
        } catch (SQLException throwables) {
            throw new DbException("Error during DB operation", throwables); // obalime puvodni vyjimku nasi runtime vyjimkou
        }
    }

    public List<Task> findAll() {
        try (Connection con = openConnection()) {
            PreparedStatement s = con.prepareStatement("SELECT id, description, due_date, done FROM tasks ORDER BY id");
            ResultSet rs = s.executeQuery();
            List<Task> tasks = new ArrayList<>();
            while (rs.next()) {
                // zpracovavame jeden radek z vysledku dotazu
                Task t = new Task(rs.getLong("id"), rs.getString("description"), rs.getDate("due_date"), rs.getBoolean("done"));
                tasks.add(t);
            }
            con.close();
            return tasks;
        } catch (SQLException throwables) {
            throw new DbException("Error during DB operation", throwables); // obalime puvodni vyjimku nasi runtime vyjimkou
        }
    }

    private Connection  openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root", "");
    }
}