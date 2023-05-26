package com.example.red2.repository;

import com.example.red2.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u.action FROM User u WHERE u.id = :id")
    boolean getAction(Long id);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    User getById(Long id);
}
