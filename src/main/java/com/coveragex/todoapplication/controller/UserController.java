package com.coveragex.todoapplication.controller;

import com.coveragex.todoapplication.dto.request.UserAuthRequestDTO;
import com.coveragex.todoapplication.dto.request.UserUpdateRequestDTO;
import com.coveragex.todoapplication.dto.response.AuthSuccessDTO;
import com.coveragex.todoapplication.entity.User;
import com.coveragex.todoapplication.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<AuthSuccessDTO> registerUser(@Valid @RequestBody UserAuthRequestDTO userAuthRequestDTO, HttpServletResponse response)
        {
            userService.saveUser(userAuthRequestDTO, response);
            AuthSuccessDTO success = new AuthSuccessDTO(true, "User registered successfully");
            return ResponseEntity.ok().body(success);
        }

    @PostMapping("/login")
    public ResponseEntity<AuthSuccessDTO> loginUser(@Valid @RequestBody UserAuthRequestDTO userAuthRequestDTO, HttpServletResponse response){
        userService.userLogin(userAuthRequestDTO, response);
        AuthSuccessDTO success = new AuthSuccessDTO(true, "User logged in successfully");
        return ResponseEntity.ok().body(success);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthSuccessDTO> logoutUser(HttpServletResponse response){
        userService.userLogout(response);
        AuthSuccessDTO success = new AuthSuccessDTO(true, "User logged out successfully");
        return ResponseEntity.ok().body(success);
    }



    @PutMapping("/update")
    public ResponseEntity<AuthSuccessDTO> updateUser(@Valid @RequestBody UserUpdateRequestDTO userUpdateRequestDTO, @AuthenticationPrincipal User user){
        User updatedUser = userService.updateUser(userUpdateRequestDTO, user);
        AuthSuccessDTO success = new AuthSuccessDTO(true, updatedUser);
        return ResponseEntity.ok().body(success);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<AuthSuccessDTO> deleteUser(@AuthenticationPrincipal User user, HttpServletResponse response){
        userService.deleteUser(user.getUsername(), response);
        AuthSuccessDTO success = new AuthSuccessDTO(true, "User deleted successfully");
        return ResponseEntity.ok().body(success);
    }

    @PutMapping("/card-limit/{limit}")
    public ResponseEntity<AuthSuccessDTO> updateCardLimit(@PathVariable int limit, @AuthenticationPrincipal User user){
        User updatedUser = userService.updateCardLimit(limit, user);
        AuthSuccessDTO success = new AuthSuccessDTO(true, updatedUser);
        return ResponseEntity.ok().body(success);
    }


    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/test2")
    public String test2() {
        return "test2";
    }

}
