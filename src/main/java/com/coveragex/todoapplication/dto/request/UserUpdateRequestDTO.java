package com.coveragex.todoapplication.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UserUpdateRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
