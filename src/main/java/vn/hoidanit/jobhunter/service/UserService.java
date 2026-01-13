package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User handleCreateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);

    }

    public List<User> handleGetAllUsers() {
        return this.userRepository.findAll();

    }

    public Optional<User> handleGetUserById(Long id) {

        return this.userRepository.findById(id);
    }

    public User handleUpdateUser(User userInput) {
        Optional<User> userLast = this.handleGetUserById(userInput.getId());
        if (userLast.isPresent()) {
            User userCurrent = userLast.get();
            userCurrent.setName(userInput.getName());
            userCurrent.setEmail(userInput.getEmail());
            userCurrent.setPassword(userInput.getPassword());
            this.handleCreateUser(userCurrent);
            return userCurrent;
        }
        return null;

    }

    public void handleDeleteUser(Long id) {
        this.userRepository.deleteById(id);
    }
}
