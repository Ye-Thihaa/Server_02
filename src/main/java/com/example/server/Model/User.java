package com.example.server.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "uitusers")
public class User {

    @Id // This annotation marks 'id' as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This makes the ID auto-increment
    private Long id;
    private String name;
    private String studentId;
    private String email;
    private String password;

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
