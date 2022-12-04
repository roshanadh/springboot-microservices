package np.roshanadh.orderservice.service;

import lombok.RequiredArgsConstructor;
import np.roshanadh.orderservice.dto.OrderLineItemsDto;
import np.roshanadh.orderservice.dto.OrderRequest;
import np.roshanadh.orderservice.model.Order;
import np.roshanadh.orderservice.model.OrderLineItems;
import np.roshanadh.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

  private final OrderRepository orderRepository;

  public void placeOrder(OrderRequest orderRequest) {
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());

    var orderLineItems = orderRequest.getOrderLineItemsDtoList()
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());

    order.setOrderLineItemsList(orderLineItems);

    orderRepository.save(order);
  }

  private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
    OrderLineItems orderLineItems = new OrderLineItems();
    orderLineItems.setPrice(orderLineItemsDto.getPrice());
    orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
    orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
    return orderLineItems;
  }
}
