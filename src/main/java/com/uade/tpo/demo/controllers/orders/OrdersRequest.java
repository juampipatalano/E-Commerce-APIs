package com.uade.tpo.demo.controllers.orders;
import java.time.LocalDate;
import lombok.Data;
import java.util.List;

@Data
public class OrdersRequest{
    private LocalDate date;
    private Double totalPrice;
    private String shippingAddress;
    private String paymentMethod;
    private List<Long> productsId; // Lista de ids de productos, al producto lo encuentro dentro del servicio
    private Long userId;

    @Data
    public static class ItemRequest {
        private Long productId;
        private Integer quantity;
    }
}

