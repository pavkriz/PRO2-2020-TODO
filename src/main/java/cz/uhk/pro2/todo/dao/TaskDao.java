package cz.uhk.pro2.todo.dao;

import cz.uhk.pro2.todo.model.Task;
import cz.uhk.pro2.todo.model.TaskList;

import java.sql.*;

public class TaskDao {

    public void save(Task task) {
        try (Connection con = openConnection()) {
            PreparedStatement s;
            boolean isInsert = false;
            if (task.getId() == 0) {
                isInsert = true;
                s = con.prepareStatement("INSERT INTO task (description, dueDate, done) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            } else {
                s = con.prepareStatement("UPDATE task set description=?, dueDate=?, done=? WHERE id=?");
                s.setLong(4, task.getId());
            }
            s.setString(1, task.getDescription());
            s.setDate(2, new Date(task.getDueDate().getTime()));
            s.setBoolean(3, task.isDone());

            ResultSet rs = s.executeQuery();

            if (isInsert && rs.next()) {
                task.setId(rs.getLong(1));
            }
        } catch (Throwable e) {
            throw new DBException("Chyba databáze!", e);
        }
    }

    public void delete(Task task) {
        delete(task.getId());
    }

    public void delete(long id) {
        try (Connection con = openConnection()) {
            PreparedStatement s;
            if (id != 0) {
                s = con.prepareStatement("DELETE FROM task WHERE id=?");
            } else {
                return;
            }
            s.setLong(1, id);
            ResultSet rs = s.executeQuery();
        } catch (Throwable e) {
            throw new DBException("Chyba databáze!", e);
        }
    }

    public TaskList findAll() {
        try (Connection con = openConnection()) {
            PreparedStatement s = con.prepareStatement("SELECT id, description, dueDate, done FROM task ORDER BY id");
            ResultSet rs = s.executeQuery();
            TaskList taskList = new TaskList();
            while (rs.next()) {
                Task t = new Task(rs.getLong("id"), rs.getString("description"), rs.getDate("dueDate"), rs.getBoolean("done"));
                taskList.addTask(t);
            }
            return taskList;
        } catch (Throwable e) {
            throw new DBException("Chyba databáze!", e);
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mariadb://localhost:3306/test", "root", "stargate");
    }
}