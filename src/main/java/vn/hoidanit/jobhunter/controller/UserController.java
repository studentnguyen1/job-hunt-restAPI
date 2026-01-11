package vn.hoidanit.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{user-id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable("user-id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetUserById(id));
    };

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllUsers());
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User userPostman) {

        User newUser = this.userService.handleCreateUser(userPostman);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);

    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User userInput) {
        User userCurrent = this.userService.handleUpdateUser(userInput);
        return ResponseEntity.status(HttpStatus.OK).body(userCurrent);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete Succesfullly");
    }
}
