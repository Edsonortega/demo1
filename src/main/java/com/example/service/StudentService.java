package com.example.service;

import com.example.entity.Student;
import com.example.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository repository;
    private final RedisService redisService;

    public StudentService(StudentRepository repository, RedisService redisService){
        this.repository = repository;
        this.redisService = redisService;
    }

    public Student saveStudent(Student s) {
        logger.info("Saving student: {}", s);
        Student savedStudent = repository.save(s);
        //redisService.setStudent("Student: " + savedStudent.getId(), savedStudent);
        return savedStudent;
    }

    public List<Student> getAll() {
        logger.info("Fetching all students");
        return repository.findAll();
    }

    public Optional<Student> getById(String id){
        String key = "Student: " + id;
        logger.info("Fetching student by id: {}", id);

        /*
        Student student = redisService.getStudent(key);
        if(student != null) {
            logger.info("Student found in Redis: {}", id);
            return Optional.of(student);
        }

        Optional<Student> studentOptional = repository.findById(id);
        if(studentOptional.isPresent()) {
            Student s = studentOptional.get();
            //redisService.setStudentWithExpiration(key, s, 1, TimeUnit.HOURS);
            return Optional.of(s);
        }
        else{
            logger.error("Student not found with id: {}", id);
            return Optional.empty();
        }
         */
        return repository.findById(id);
    }

    public void deleteStudent(String id) {
        logger.info("Deleting student with id: {}", id);
        repository.deleteById(id);
        //redisService.deleteStudent("Student: " + id);
    }

    public Student findByFirstName(String name) {
        logger.info("Fetching student by name: {}", name);
        return repository.findByFirstName(name);
    }
}
