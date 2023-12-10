package lk.ijse.dep11.app.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lk.ijse.dep11.app.to.TaskTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        System.out.println("updateTask");
    }

    @GetMapping
    public void getAllTasks(){
        System.out.println("getAllTasks");
    }

    @DeleteMapping("/{id}")
    public void deleteTask(){
        System.out.println("deleteTask");
    }



}
