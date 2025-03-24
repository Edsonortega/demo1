package com.example.controller;

import com.example.dto.StudentDto;
import com.example.entity.Student;
import com.example.service.StudentGradeService;
import com.example.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;
    private final StudentGradeService studentGradeService;

    public StudentController(StudentService studentService, StudentGradeService studentGradeService){
        this.studentService = studentService;
        this.studentGradeService = studentGradeService;
    }

    /*
    @Value("${app.name:DefaultName}")
    private String appName;
     */

    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody StudentDto request){
        Student student = new Student();
        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());
        student.setGrades(request.grades());

        Student saveStudent = studentService.saveStudent(student);
        return ResponseEntity.ok(saveStudent);
    }

    //Get All Students
    @GetMapping
    public List<Student> getAllStudents(){
        return studentService.getAll();
    }

    //Get By Id
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id){
        Optional<Student> s = studentService.getById(id);
        /*
        if (s.isPresent()) {
            return ResponseEntity.ok(s.get());
        } else {
            return ResponseEntity.notFound().build();
        }
        */
        return s.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Update student
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable String id, @RequestBody Student student){
        Optional<Student> currentStudent = studentService.getById(id);
        if(currentStudent.isPresent()){
            Student updateStudent = currentStudent.get();
            updateStudent.setFirstName(student.getFirstName());
            updateStudent.setLastName(student.getLastName());
            updateStudent.setEmail(student.getEmail());

            Student saveStudent = studentService.saveStudent(updateStudent);
            return ResponseEntity.ok(saveStudent);
        } else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id){
        Optional<Student> student = studentService.getById(id);
        if(student.isPresent()){
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/add-grade")
    public ResponseEntity<Student> addGrade(@PathVariable String id, @RequestParam double grade){
        Student student = studentGradeService.addGrade(id, grade);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/avarage")
    public ResponseEntity<Double> getAvarageGrade(@PathVariable String id){
        Double avg = studentGradeService.calculateAverage(id);
        return avg != null ? ResponseEntity.ok(avg) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/highest-grade")
    public ResponseEntity<Double> getHighestGrade(@PathVariable String id){
        Double highest = studentGradeService.getHighestScore(id);
        return highest != null ? ResponseEntity.ok(highest) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/lowest-grade")
    public ResponseEntity<Double> getLowestGrade(@PathVariable String id){
        Double lowest = studentGradeService.getLowestScore(id);
        return lowest != null ? ResponseEntity.ok(lowest) : ResponseEntity.notFound().build();
    }

    @GetMapping("search-by/{name}")
    public ResponseEntity<Student> getStudentByName(@PathVariable String name){
        Student student = studentService.findByFirstName(name);
        return student != null ? ResponseEntity.ok(student) : ResponseEntity.notFound().build();
    }

}
