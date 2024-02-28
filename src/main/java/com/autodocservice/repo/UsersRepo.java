package com.autodocservice.repo;

import com.autodocservice.model.Users;
import com.autodocservice.model.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepo extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

    List<Users> findAllByRole(Role role);


}