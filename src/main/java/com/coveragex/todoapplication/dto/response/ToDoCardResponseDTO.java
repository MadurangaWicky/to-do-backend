package com.coveragex.todoapplication.dto.response;

import java.time.Instant;

public class ToDoCardResponseDTO {
    private Long id;
    private String title;
    private String description;
    public Instant createdAt;

    public Long userId;
    public Instant doneAt;
    public Instant deletedAt;
    public boolean isDone;
    public boolean isDeleted;

    public ToDoCardResponseDTO() {
    }

    public ToDoCardResponseDTO(Long id, String title, String description, Instant createdAt, Long userId, Instant doneAt, Instant deletedAt, boolean isDone, boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.userId = userId;
        this.doneAt = doneAt;
        this.deletedAt = deletedAt;
        this.isDone = isDone;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Instant getDoneAt() {
        return doneAt;
    }

    public void setDoneAt(Instant doneAt) {
        this.doneAt = doneAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
