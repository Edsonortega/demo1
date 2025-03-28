package com.example.service;

import com.example.entity.Student;
import com.example.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentGradeService {

    private final StudentRepository repository;

    public StudentGradeService(StudentRepository repository){
        this.repository = repository;
    }

    public Student addGrade(String id, double grade){
        Optional<Student> student = repository.findById(id);
        if(student.isPresent()){
            Student s = student.get();
            s.getGrades().add(grade);
            return repository.save(s);
        }
        return null;
    }

    public Double calculateAverage(String id) {
        Optional<Student> student = repository.findById(id);
        if (student.isPresent()) {
            List<Double> grades = student.get().getGrades();
            if (grades == null || grades.isEmpty()) {
                return null;
            }
            return grades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }
        return null;
    }

    public Double getLowestScore(String id){
        Optional<Student> student = repository.findById(id);
        return student.flatMap(s -> s.getGrades().stream().min(Double::compareTo)).orElse(null);
    }

    public Double getHighestScore(String id){
        Optional<Student> student = repository.findById(id);
        return student.flatMap(s -> s.getGrades().stream().max(Double::compareTo)).orElse(null);
    }
}
