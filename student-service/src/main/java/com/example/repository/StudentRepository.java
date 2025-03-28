package com.example.repository;

import com.example.entity.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {

    @Query("{ 'firstName':?0}"  )
    Student findByFirstName(String name);
}
