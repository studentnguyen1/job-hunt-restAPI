package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.LoginDTO;
import vn.hoidanit.jobhunter.domain.dto.RestLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@RestController
@RequestMapping("/api/v1")

public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<RestLoginDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // set thong tin nguoi dung vao Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // create a token
        RestLoginDTO res = new RestLoginDTO();
        User currentUser = this.userService.handleGetUserByUsername(loginDTO.getUsername());
        if (currentUser != null) {
            RestLoginDTO.UserLogin userLogin = new RestLoginDTO.UserLogin(currentUser.getId(), currentUser.getEmail(),
                    currentUser.getName());
            res.setUser(userLogin);
        }
        String access_token = this.securityUtil.createAccessToken(authentication, res.getUser());
        res.setAccessToken(access_token);
        // create refresh token
        String refresh_token = this.securityUtil.createRefreshToken(loginDTO.getUsername(), res);

        // update user
        this.userService.updateUserToken(refresh_token, loginDTO.getUsername());

        // response cookie
        ResponseCookie resCookies = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookies.toString()).body(res);

    }

    @GetMapping("/auth/account")
    public ResponseEntity<RestLoginDTO.UserLogin> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

        RestLoginDTO.UserLogin userLogin = new RestLoginDTO.UserLogin();

        User currentUser = this.userService.handleGetUserByUsername(email);
        if (currentUser != null) {
            userLogin.setId(currentUser.getId());
            userLogin.setEmail(currentUser.getEmail());
            userLogin.setUsername(currentUser.getName());
        }
        return ResponseEntity.ok().body(userLogin);

    }
}