package com.uade.tpo.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.controllers.users.UserRequest;
import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.repository.OrderRepository;
import com.uade.tpo.demo.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUser(String email, UserRequest userRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());

        return userRepository.save(user);
    }

    @Override
    public List<Order> getUserOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        System.out.println("user id: " + user.getId());
        return orderRepository.findByUserId(user.getId());
    }
    
}
