package vn.hoidanit.jobhunter.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

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
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(@Filter Specification<User> spec, Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleGetAllUsers(spec, pageable));
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
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (id > 1501) {
            throw new IdInvalidException("Id khong lon hon 15");
        }

        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete Succesfullly");
    }
}
