package com.example.dto;

import java.util.List;

public record StudentDto(
        String firstName,
        String lastName,
        String email,
        List<Double> grades
) {}
