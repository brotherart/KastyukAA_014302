package com.autodocservice.controller;

import com.autodocservice.model.Users;
import com.autodocservice.model.enums.Role;
import com.autodocservice.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}")
public class UsersCont {

    @Autowired
    protected UsersRepo usersRepo;

    @GetMapping()
    public ResponseEntity<?> user(@PathVariable Long userId) {
        Users user;
        try {
            user = usersRepo.findById(userId).orElseThrow();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/edit")
    public ResponseEntity<?> userEdit(@PathVariable Long userId, @RequestParam Role role) {
        Users user;
        try {
            user = usersRepo.findById(userId).orElseThrow();
            if (user.getRole() == Role.ADMIN) {
                if (usersRepo.findAllByRole(Role.ADMIN).size() == 1) {
                    return new ResponseEntity<>("В системе должен быть 1 админ", HttpStatus.BAD_REQUEST);
                }
            }
            user.setRole(role);
            user = usersRepo.saveAndFlush(user);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
