package com.coveragex.todoapplication.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;


//I didn't use lombok. my IDE has an issue with the annotation process. so i did it manually


@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public int getCardListLimit() {
        return cardListLimit;
    }

    public void setCardListLimit(int cardListLimit) {
        this.cardListLimit = cardListLimit;
    }

    @Column(nullable = false, unique = true)
    private String username;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name = "";

    @Column(nullable = false)
    private String password;


    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User() {
    }

    public User(Long id, String username, String name, String password, Set<ToDoCard> toDoCards, int cardListLimit) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.password = password;
        this.toDoCards = toDoCards;
        this.cardListLimit = cardListLimit;
    }

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<ToDoCard> toDoCards = new HashSet<>();

    @Column(name = "card_list_limit")
    private int cardListLimit = 5;


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", cardListLimit=" + cardListLimit +
                '}';
    }
}
