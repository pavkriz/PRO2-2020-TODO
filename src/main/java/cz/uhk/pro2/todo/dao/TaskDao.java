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
                // TODO insert
                p = con.prepareStatement("INSERT INTO task (id, description, due_date, done) VALUES "
                        + "(DEFAULT," + task.getDescription() + "," + task.getDueDate() + "," + task.isDone() + ")");
                p.executeQuery();
            } else {
                //TODO update
                p = con.prepareStatement("UPDATE TABLE task SET Description=" +
                        task.getDescription() + ", due_date=" + task.getDueDate() +
                        ", done=" + task.isDone() + " WHERE id=" + task.getId());
                p.executeQuery();
            }
            p.close();
        } catch (SQLException throwables) {
            throw new DbException("Error during DB operation", throwables); // obalime puvodni vyjimku nasi runtime vyjimkou
        }
    }

        public void delete( long id){
            //TODO 24.11.
            try (Connection con = openConnection()) {
                PreparedStatement p = con.prepareStatement("DELETE from test where id=" + id);
                p.executeQuery();
                p.close();
            } catch (SQLException throwables) {
                throw new DbException("Error during DB operation", throwables);
            }
        }

        public static List<Task> findAll() {
            try (Connection con = openConnection()) {
                PreparedStatement s = con.prepareStatement("SELECT id, description, due_date, done FROM task2 ORDER BY id");
                ResultSet rs = s.executeQuery();
                List<Task> tasks = new ArrayList<>();
                while (rs.next()) {
                    // zpracovavame jeden radek z vysledku dotazu
                    Task t = new Task(rs.getLong("id"), rs.getString("Description"), rs.getDate("due_date"), rs.getBoolean("done"));
                    tasks.add(t);
                }
                s.close();
                return tasks;
            } catch (SQLException throwables) {
                throw new DbException("Error during DB operation", throwables);
            }
        }

        private static Connection openConnection() throws SQLException {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "my-secret-pw");
    }
}
