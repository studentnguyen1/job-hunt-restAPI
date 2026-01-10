package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/create")
    public String createNewUser() {
        User newUser = new User();
        newUser.setEmail("NGuyen@gmail.com");
        newUser.setName("NGuyen");
        newUser.setPassword("nguyen");
        this.userService.handleCreateUser(newUser);
        return "createUser";

    }
}
