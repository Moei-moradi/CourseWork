package com.example.coursework.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Food extends Product {
    private int age; // Added age attribute
    private String foodType;

    public Food(String title, String description, int qty, float price, String manufacturer, float weight, int age, String foodType) {
        super(title, description, qty, price, manufacturer, weight);
        this.age = age;
        this.foodType = foodType;
    }
}
