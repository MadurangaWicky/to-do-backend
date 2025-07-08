package com.coveragex.todoapplication.dto.request;

public class ToDoCardCreateRequestDTO {
    private String title;

    public String getTitle() {
        return title;
    }

    public ToDoCardCreateRequestDTO() {
    }

    @Override
    public String toString() {
        return "ToDoCardCreateRequestDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ToDoCardCreateRequestDTO(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

}
