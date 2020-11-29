package cz.uhk.pro2.todo.dao;

import cz.uhk.pro2.todo.model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    public void save(Task task) {
        // TODO DU 24.11., pouzivejte parametry v prepared statementu ( ?, ?)
        if (task.getId() == 0) {
            // TODO INSERT
            insert(task);
        } else {
            // TODO UPDATE
            update(task);
        }
    }

    protected void insert(Task task) {
        try {
            Connection con = openConnection();
            PreparedStatement ps = con.prepareStatement("insert into task2 (description, due_date, done) values (?,?,?)", new String[] {"id"});
            ps.setString(1,task.getDescription());
            ps.setDate(2,new java.sql.Date(task.getDueDate().getTime()));
            ps.setBoolean(3, task.isDone());
            ps.executeUpdate();

            ResultSet res = ps.getGeneratedKeys();
            res.next();
            long id = res.getLong(1);
            task.setId(id);

            ps.close();
        } catch (SQLException throwable) {
            throw new DbException("Error during DB operation (insert task)", throwable);         //throw new??
        }
    }

    private void update(Task task) {
        try {
            Connection con = openConnection();
            Statement st = con.createStatement();
            st.executeUpdate(String.format("update task2 set description='%s', due_date='%s', done=%s where id=%d",             //moc se mi to nelibi
                    task.getDescription(),task.getDueDate(),task.isDone(),task.getId()));
            st.close();
        } catch (SQLException throwable) {
            throw new DbException("Error during DB operation (update task)", throwable);         //throw new??
        }

    }

    public void delete(Task task) {
        delete(task.getId());
    }

    public void delete(long id) {
        // TODO DU 24.11.
        try(Connection con = openConnection()) {
            Statement statement = con.createStatement();
            statement.execute(String.format("delete FROM task2 where id=%d", id));
            statement.close();
        } catch (SQLException throwables) {
            throw new DbException("Error during DB operation (delete task)", throwables);         //throw new??
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
            s.close();
            return tasks;
        } catch (SQLException throwables) {
            throw new DbException("Error during DB operation", throwables); // obalime puvodni vyjimku nasi runtime vyjimkou
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/pro2_todo","root", "unicornPR");
    }
}
