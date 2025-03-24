package com.example.service;

import com.example.dto.StudentDto;
import com.example.entity.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.Arrays;
import java.util.Objects;

@Service
public class ExternalStudentService {
    private static final Logger logger = LoggerFactory.getLogger(ExternalStudentService.class);
    private final RestTemplate restTemplate;
    private final String URL =  "http://localhost:8080/api/student";

    public ExternalStudentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //GetAll
    @CircuitBreaker(name = "getStudents", fallbackMethod = "getAllStudentsFallback")
    public ResponseEntity<?> getAllStudents(){
        logger.info("Fetching all students from external API");

        // Failure
        if (Math.random() < 0.6) { // 60% chance of failure
            throw new RuntimeException("Simulated failure in getAllStudents");
        }

        ResponseEntity<Student[]> response = restTemplate.getForEntity(URL, Student[].class);
        logger.info("Successfully fetched {} students", response.getBody().length);
        return ResponseEntity.ok(Arrays.asList(Objects.requireNonNull(response.getBody())));
    }

    public ResponseEntity<String> getAllStudentsFallback(Exception e) {
        logger.error("Circuit breaker fallback: Unable to fetch students. Error: {}", e.getMessage());
        return ResponseEntity.status(503).body("Fallback: Service not available");
    }

    //GetById
    @CircuitBreaker(name = "externalStudentService", fallbackMethod = "getByIdFallback")
    @Retryable(maxAttempts = 3, value = {Exception.class}, backoff = @Backoff(delay = 2000))
    public Student getById(String id){
        String url = URL + "/" + id;
        logger.info("Fetching student with id {} from external API", id);
        Student student = restTemplate.getForObject(url, Student.class);
        logger.info("Successfully fetched student with id {}", id);
        return student;
    }

    public Student getByIdFallback(String id, Exception e) {
        logger.error("Circuit breaker fallback: Unable to fetch student with id {}. Error: {}", id, e.getMessage());
        return new Student();
    }

    //Post
    @CircuitBreaker(name = "externalStudentService", fallbackMethod = "createStudentFallback")
    public Student createStudent(StudentDto studentDto){
        logger.info("Creating new student in external API");
        HttpHeaders headers = new HttpHeaders();
        //headers.setBasicAuth("admin", "hi12");
        HttpEntity<StudentDto> request = new HttpEntity<>(studentDto, headers);

        ResponseEntity<Student> response = restTemplate.postForEntity(URL, request, Student.class);
        logger.info("Successfully created student with id {}", response.getBody().getId());
        return response.getBody();
    }

    public Student createStudentFallback(StudentDto studentDto, Exception e) {
        logger.error("Circuit breaker fallback: Unable to create student. Error: {}", e.getMessage());
        return new Student();
    }

    //Update
    @CircuitBreaker(name = "externalStudentService", fallbackMethod = "updateStudentFallback")
    public Student updateStudent(String id, StudentDto studentDto){
        String url = URL + "/" + id;
        logger.info("Updating student with id {} in external API", id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<StudentDto> requestEntity = new HttpEntity<>(studentDto, headers);
        ResponseEntity<Student> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Student.class);
        logger.info("Successfully updated student with id {}", id);
        return response.getBody();
    }

    public Student updateStudentFallback(String id, StudentDto studentDto, Exception e) {
        logger.error("Circuit breaker fallback: Unable to update student with id {}. Error: {}", id, e.getMessage());
        return new Student(); // Return an empty Student object as fallback
    }


    //Delete
    @CircuitBreaker(name = "externalStudentService", fallbackMethod = "deleteStudentFallBack")
    public void deleteStudent(String id) {
        String url = URL + "/" + id;
        logger.info("Deleting student with id {} from external API", id);
        restTemplate.delete(url);
        logger.info("Successfully deleted student with id {}", id);
    }

    public void deleteStudentFallback(String id, Exception e) {
        logger.error("Circuit breaker fallback: Unable to delete student with id {}. Error: {}", id, e.getMessage());
    }

    @CircuitBreaker(name = "externalStudentService", fallbackMethod = "getByFirstNameFallback")
    @Retryable(maxAttempts = 3, value = {Exception.class}, backoff = @Backoff(delay = 2000))
    public Student getByFirstName(String firstName) {
        String url = URL + "/search-by/" + firstName;
        logger.info("Fetching student with first name {} from external API", firstName);
        Student student = restTemplate.getForObject(url, Student.class);
        return student;
    }

    public Student getByFirstNameFallback(String firstName, Exception e) {
        logger.error("Circuit breaker fallback: Unable to fetch students with first name {}. Error: {}", firstName, e.getMessage());
        return new Student();
    }

    //Recover for retryable
    @Recover
    public Student recover(Exception e, String id) {
        logger.error("All retry attempts failed for student id: {}. Error: {}", id, e.getMessage());
        return new Student();
    }
}
