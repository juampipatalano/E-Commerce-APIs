package com.uade.tpo.demo.service;


import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.OrderDetail;
import com.uade.tpo.demo.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import com.uade.tpo.demo.repository.OrderRepository;
import com.uade.tpo.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

import com.uade.tpo.demo.repository.OrderDetailRepository;
import com.uade.tpo.demo.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional
    public Order createOrder(LocalDate date, String shippingAddress, String paymentMethod, List<Long> productsId, Long userId) {
        Optional<User> resultUser = userRepository.findById(userId);
        if (resultUser.isEmpty()) {//NO EXISTE USUARIO CON DICHO ID
            throw new IllegalArgumentException("No se ha encontrado un usuario con el id: " + userId);
        }
        else{//ENCUENTRA EL USUARIO
            //CALCULO EL PRECIO TOTAL DE LA ORDEN
            Double totalPrice = 0.0;
            for (Long productId: productsId) {
                Optional<Product> resultProduct = productRepository.findById(productId);
                Product product = resultProduct.get();
                Double precio = product.getPrice();
                totalPrice += precio;
            }
            
            User user = resultUser.get();
            Order order = new Order(date, totalPrice, shippingAddress, paymentMethod, user); //CREO LA ORDEN
            orderRepository.save(order); //GUARDO LA ORDEN


            //RECORRO LA LISTA DE IDS DE PRODUCTOS PARA CREAR LOS DETALLES DE LA ORDEN
            for (Long productId : productsId) {
                if (orderDetailRepository.getOrderDetailByOrderAndProductId(order.getId(), productId).isEmpty()){//NO ENCUENTRA REPETIDO EL DETALLE DE LA ORDEN
                    Optional<Product> resultProduct = productRepository.findById(productId);
                    Product product = resultProduct.get();
                    OrderDetail orderDetail = new OrderDetail(order, product, 1L); //CREO UN NUEVO DETALLE DE ORDEN

                    orderDetailRepository.save(orderDetail);        //GUARDO EL DETALLE
                    
                    order.getOrderDetails().add(orderDetail);       //AGREGO EL DETALLE A LA LISTA DE DETALLES DE LA ORDEN

                
                    product.decreaseStock();                        //LE DESCUENTO EL STOCK
                    productRepository.save(product);                //GUARDO EL PRODUCTO CON EL STOCK ACTUALIZADO
                }
                else{ //ENCUENTRA REPETIDO AL DETALLE DE LA ORDEN
                    Optional<Product> resultProduct = productRepository.findById(productId);
                    Product product = resultProduct.get();

                    Optional<OrderDetail> result = orderDetailRepository.getOrderDetailByOrderAndProductId(order.getId(), productId);
                    OrderDetail orderDetail = result.get();
                    //funcion de incrementar cantidad a la orden
                    orderDetail.increaseQuantity();                 //LE AUMENTO LA CANTIDAD AL DETALLE DE ORDEN
                    orderDetailRepository.save(orderDetail);        //GUARDO EL DETALLE ACTUALIZADO

                    product.decreaseStock();                        //LE DESCUENTO EL STOCK
                    productRepository.save(product);                //GUARDO EL PRODUCTO CON EL STOCK ACTUALIZADO
                }

                
            }

            return order;
        }
        
    }

    @Override
    public Page<Order> getOrders(org.springframework.data.domain.PageRequest pageRequest) {
        return orderRepository.findAll(pageRequest);
    }
    


}