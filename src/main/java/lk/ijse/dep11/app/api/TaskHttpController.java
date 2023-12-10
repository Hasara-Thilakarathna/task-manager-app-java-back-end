package lk.ijse.dep11.app.api;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.web.bind.annotation.*;

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

    public void destroy(){
        pool.close();
    }

    @PostMapping
    public void createTask(){

    }

    @PatchMapping
    public void updateTask(){

    }

    @GetMapping
    public void getAllTasks(){

    }

    @DeleteMapping
    public void deleteTask(){

    }



}
