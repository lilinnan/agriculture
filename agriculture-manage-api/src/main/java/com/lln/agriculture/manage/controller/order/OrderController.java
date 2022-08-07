package com.lln.agriculture.manage.controller.order;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.PageRequest;
import com.lln.agriculture.manage.service.order.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/11/1 16:35
 */
@RestController
@RequestMapping("/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{status}")
    @PreAuthorize("hasAuthority('ORDER_SELECT')")
    public Message order(PageRequest pageRequest, @PathVariable int status) {
        return orderService.selectAllOrder(pageRequest, status);
    }

    @PutMapping("/delivery")
    @PreAuthorize("hasAuthority('ORDER_UPDATE')")
    public Message delivery(@Valid @RequestBody OrderService.DeliveryData deliveryData) {
        return orderService.delivery(deliveryData);
    }
}
