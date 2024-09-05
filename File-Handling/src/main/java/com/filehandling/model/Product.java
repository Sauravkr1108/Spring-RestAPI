package com.filehandling.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "productdb")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private Double price;

    private String imageName;
}
