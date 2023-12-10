package lk.ijse.dep11.app.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.dep11.app.to.TaskTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PreDestroy;
import java.sql.*;

@RestController
@RequestMapping("api/v1/tasks")

public class TaskHttpController {

    private final HikariDataSource pool;

    public TaskHttpController(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/taskvista_todo_app");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("postgres");
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        hikariConfig.addDataSourceProperty("maximumPoolSize", 10);
        pool = new HikariDataSource(hikariConfig);
    }

    @PreDestroy
    public void destroy(){
        pool.close();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json", consumes = "application/json")
    public TaskTO createTask(@RequestBody @Validated TaskTO task){
       try(Connection connection = pool.getConnection()) {
           PreparedStatement stm = connection
                   .prepareStatement("INSERT INTO task (description, status, email) VALUES (?,FALSE,?)",
                           Statement.RETURN_GENERATED_KEYS);
           stm.setString(1,task.getDescription());
           stm.setString(2,task.getEmail());
           stm.executeUpdate();
           ResultSet generatedKeys = stm.getGeneratedKeys();
           generatedKeys.next();
           int id = generatedKeys.getInt(1);
           task.setId(id);
           task.setStatus(false);
           return task;

       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }

    @PatchMapping("/{id}")
    public void updateTask(){

    }

    @GetMapping
    public void getAllTasks(){
        System.out.println("getAllTasks");
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable("id") int taskId){
        try(Connection connection = pool.getConnection()) {
            PreparedStatement stmExist = connection
                    .prepareStatement("SELECT * FROM task WHERE id = ?");
            stmExist.setInt(1,taskId);

            if(!stmExist.executeQuery().next()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Task not found");
            }

            PreparedStatement stmDelete = connection
                    .prepareStatement("DELETE FROM task WHERE id = ?");
            stmDelete.setInt(1,taskId);
            stmDelete.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



}
