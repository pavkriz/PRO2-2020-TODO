package cz.uhk.pro2.todo.dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.uhk.pro2.todo.model.Task;

public class TaskDao {
    public void save(Task task) throws ParseException {
        // TODO DU 24.11., pouzivejte parametry v prepared statementu ( ?, ?)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format(task.getDueDate());
                System.out.println(date);
               
        try (Connection con = openConnection()) {
            if (task.getId() == 0) {
                
                PreparedStatement stat = con.prepareStatement("INSERT INTO task2 (description,due_date,done) VALUES "
                        + "('" + task.getDescription() + "','" + date + "','" + 0 + "')");
                stat.executeUpdate();
            } else {
                // TODO UPDATE
            }
        } catch (SQLException throwables) {
            throw new DbException("Error while saving", throwables);
        } 
    }

    public void delete(Task task) {
        delete(task.getId());
    }

    public void delete(long id) {
        // TODO DU 24.11.
        try(Connection con=openConnection()){
            PreparedStatement stat = con.prepareStatement("DELETE FROM task2 where id="+id);
            stat.executeUpdate();
            System.out.println("Deleted");
        }
        catch (SQLException throwables) {
            throw new DbException("Error while deleting", throwables); 
        }
    }

    public List<Task> findAll() {
        try (Connection con = openConnection()) {
            
            PreparedStatement s = con.prepareStatement("SELECT id, description, due_date, done FROM task2 ORDER BY id");
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
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","");
    }
}