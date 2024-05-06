package com.example.coursework.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Medicine extends Product {
    private String medicineType;
    private LocalDate expiryDate;

    public Medicine(String title, String description, int qty, String manufacturer, String medicineType, LocalDate expiryDate) {
        super(title, description, qty, manufacturer);
        this.medicineType = medicineType;
        this.expiryDate = expiryDate;
    }

}
