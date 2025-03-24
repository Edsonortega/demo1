package com.example.service;

import com.example.entity.Task;
import com.example.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task t){
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(t.getTitle());
                    task.setDescription(t.getDescription());
                    task.setCompleted(t.isCompleted());
                    return saveTask(task);
                }).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }
}
