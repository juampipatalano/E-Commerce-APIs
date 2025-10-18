package com.uade.tpo.demo.controllers.users;

import java.security.Security;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private String getUserEmail(){
        // Obtengo el contexto de seguridad actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //Recupero el email del usuario autenticado
        return authentication.getName();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(){
        String email = getUserEmail();
        return userService.getUserByEmail(email)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/profile")
    public ResponseEntity<User> updateUserProfile(@Valid @RequestBody UserRequest userRequest){
        String email = getUserEmail();
        User updatedUser = userService.updateUser(email, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getUserOrders(){
        String email = getUserEmail();
        List<Order> orders = userService.getUserOrders(email);
        return ResponseEntity.ok(orders);
    }




    
}
