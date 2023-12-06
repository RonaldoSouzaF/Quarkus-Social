package org.acme.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Name_Is_Required")
    private String name;
    @NotNull(message = "Age_Is_Required")
    private Integer age;

}

