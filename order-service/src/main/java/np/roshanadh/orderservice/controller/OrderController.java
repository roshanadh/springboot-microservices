package np.roshanadh.orderservice.controller;

import lombok.RequiredArgsConstructor;
import np.roshanadh.orderservice.dto.OrderRequest;
import np.roshanadh.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String placeOrder(@RequestBody OrderRequest orderRequest) {
    orderService.placeOrder(orderRequest);
    return "Order Placed Successfully";
  }
}
