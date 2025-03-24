package com.example.controller;

import com.example.dto.StudentDto;
import com.example.entity.Student;
import com.example.service.ExternalStudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external-student")
public class ExternalStudentController {

    private final ExternalStudentService externalStudentService;
    public ExternalStudentController(ExternalStudentService externalStudentService) {
        this.externalStudentService = externalStudentService;
    }

    @GetMapping
    public ResponseEntity<?> getAllExternalStudents() {
        return externalStudentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getExternalStudentById(@PathVariable String id) {
        return ResponseEntity.ok(externalStudentService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Student> createExternalStudent(@RequestBody StudentDto student) {
        return ResponseEntity.ok(externalStudentService.createStudent(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateExternalStudent(@PathVariable String id, @RequestBody StudentDto student) {
        return ResponseEntity.ok(externalStudentService.updateStudent(id, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExternalStudent(@PathVariable String id) {
        externalStudentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search-by/{firstName}")
    public ResponseEntity<Student> getExternalStudentByFirstName(@PathVariable String firstName) {
        return ResponseEntity.ok(externalStudentService.getByFirstName(firstName));
    }
}
