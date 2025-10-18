package com.uade.tpo.demo.service;
import java.util.Optional;

import com.uade.tpo.demo.controllers.users.UserRequest;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.entity.Order;
import java.util.List;

public interface UserService {
    Optional<User> getUserByEmail(String email);
    User updateUser(String email, UserRequest userRequest);
    List<Order> getUserOrders(String email);
}
