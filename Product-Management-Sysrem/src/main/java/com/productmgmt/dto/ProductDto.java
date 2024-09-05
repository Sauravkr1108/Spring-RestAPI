package com.productmgmt.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductDto {

    private Integer id;

    @NotBlank(message = "Name can't be blank")
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Only alphabets")
    private String name;

    @NotEmpty
    @Size(min = 3, max = 10, message = "Description size 3-10")
    private String description;

    @NotEmpty
    @Pattern(regexp = "^[0-9]*$", message = "Invalid price")
    private String price;

    private Integer quantity;

    @Email
    private String email;
}
