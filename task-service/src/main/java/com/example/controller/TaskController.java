package com.example.controller;

import com.example.entity.Task;
import com.example.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> response = taskService.getTaskById(id);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.saveTask(task));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        Optional<Task> response = taskService.getTaskById(id);
        if(response.isPresent()){
            Task taskToUpdate = response.get();
            taskToUpdate.setTitle(updatedTask.getTitle());
            taskToUpdate.setDescription(updatedTask.getDescription());
            taskToUpdate.setPriority(updatedTask.getPriority());
            return ResponseEntity.ok(taskService.saveTask(taskToUpdate));
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Optional<Task> response = taskService.getTaskById(id);
        if(response.isPresent()){
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<Task> getTaskByTitle(@RequestParam String title) {
        Optional<Task> response = taskService.getTaskByTitle(title);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
