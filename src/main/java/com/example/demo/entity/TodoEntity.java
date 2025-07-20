package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "todo_app")
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    private boolean completed;

    // Mỗi Todo thuộc về một User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // tên cột khóa ngoại trong bảng todo_app
    private UserEntity user;

    // --- GETTER & SETTER ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}