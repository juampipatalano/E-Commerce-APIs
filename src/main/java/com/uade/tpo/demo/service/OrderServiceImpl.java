package com.uade.tpo.demo.service;


import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.OrderDetail;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.controllers.orders.OrdersRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


import java.time.LocalDate;


import org.springframework.beans.factory.annotation.Autowired;
import com.uade.tpo.demo.repository.OrderRepository;
import com.uade.tpo.demo.repository.UserRepository;
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
    private UserRepository UserRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    @Override
    public void deleteOrder(Long orderId, Long userId) {}

    @Override
    /*FALTA:
    CREAR USER REPOSITORY
    CREAR ORDERDETAIL REPOSITORY (incluye crear los SELECT en la db para hacer el getOrderDetailByOrderAndProductId)
    VER FUNCION DE INCREMENTAR CANTIDAD PARA LA ORDERDETAIL

    */ 

    public Order createOrder(LocalDate date, String shippingAddress, String paymentMethod, Double totalPrice, List<Long> productsId, Long userId) {
        Optional<User> resultUser = UserRepository.findById(userId);
        if (resultUser.isEmpty()) {//NO EXISTE USUARIO CON DICHO ID
            throw new IllegalArgumentException("No se ha encontrado un usuario con el id: " + userId);
        }
        else{//ENCUENTRA EL USUARIO
            User user = resultUser.get();
            Order order = new Order(date, totalPrice, shippingAddress, paymentMethod, user); //CREO LA ORDEN
            orderRepository.save(order);

            for (Long productId : productsId) {
                if (orderDetailRepository.getOrderDetailByOrderAndProductId(order.getId(), productId).isEmpty()){//NO ENCUENTRA REPETIDO EL DETALLE DE LA ORDEN
                    Optional<Product> resultProduct = productRepository.findById(productId);
                    Product product = resultProduct.get();
                    OrderDetail orderDetail = new OrderDetail(order, product, 1L); //CREO UN NUEVO DETALLE DE ORDEN

                    orderDetailRepository.save(orderDetail);        //GUARDO EL DETALLE

                   // order.getOrderDetails().add(orderDetail);       //AGREGO EL DETALLE AL LISTADO DE DETALLES DE LA ORDEN


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
    


}