package com.springboot.dao;

import com.springboot.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactDao extends JpaRepository<Contact, Integer> {

    @Query("from Contact c where c.user.id=:userId")
    public List<Contact> findContactByUser(@Param("userId") int userId);
}
