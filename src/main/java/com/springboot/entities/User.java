package com.springboot.entities;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank(message = "invalid username")
    @Size(min = 3, max = 12, message = "username must be 5-12 chars long")
    private String name;

    @Column(unique = true)
    @Email(regexp = "[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    //    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\\\S+$).{8,20}$")
    @Size(min = 3, message = "password must be 5 char long")
    private String password;

    private String role;
    private boolean status;
    private String profile;

    @Column(length = 500)
    private String about;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    public User() {
    }

    public User(String name, String email, String password, String role, boolean status, String profile, String about) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.profile = profile;
        this.about = about;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", status=" + status +
                ", profile='" + profile + '\'' +
                ", about='" + about + '\'' +
                ", contacts=" + contacts +
                '}';
    }
}
