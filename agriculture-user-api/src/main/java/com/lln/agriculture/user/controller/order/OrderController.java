package com.lln.agriculture.user.controller.order;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.user.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/21 21:24
 */


@RestController
@RequestMapping("/v1/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Message order(@RequestBody OrderService.BuyInfo buyInfo) {
        return orderService.submitOrder(buyInfo);
    }

    @PostMapping("/pay")
    public Message pay(@RequestBody OrderService.OrderPayInfo orderPayInfo) {
        return orderService.pay(orderPayInfo);
    }


    @GetMapping("/check-pay-result")
    public Message checkResult(@RequestParam Map<String, String> params) {
        return orderService.check(params);
    }

    @PostMapping("/async-check-pay-result")
    public void asyncCheckResult(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        response.getWriter().print(orderService.asyncCheck(params));
    }

    @GetMapping("/history-order/{type}")
    public Message historyOrder(@PathVariable int type) {
        return orderService.selectHistoryOrder(type);
    }

    @GetMapping("/{order}")
    public Message queryOrder(@PathVariable String order) {
        return orderService.queryOrder(order);
    }

    @PutMapping("/confirm/{orderNum}")
    public Message confirm(@PathVariable String orderNum) {
        return orderService.confirm(orderNum);
    }

    @PutMapping("/cancel/{orderNum}")
    public Message cancel(@PathVariable String orderNum) {
        return orderService.cancel(orderNum);
    }

    @PutMapping("/delete/{orderNum}")
    public Message delete(@PathVariable String orderNum) {
        return orderService.delete(orderNum);
    }
}
