package com.coveragex.todoapplication.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UserUpdateRequestDTO {
    private String name;

    private int cardListLimit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
