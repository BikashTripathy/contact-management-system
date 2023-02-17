package com.springboot.dao;

import com.springboot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {
    public List<User> findByEmail(String email);

    @Query("select u from User u where u.email=:email")
    public User getUserByUserName(@Param("email") String email);
}
