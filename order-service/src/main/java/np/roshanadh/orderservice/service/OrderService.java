package np.roshanadh.orderservice.service;

import lombok.RequiredArgsConstructor;
import np.roshanadh.orderservice.dto.InventoryResponse;
import np.roshanadh.orderservice.dto.OrderLineItemsDto;
import np.roshanadh.orderservice.dto.OrderRequest;
import np.roshanadh.orderservice.model.Order;
import np.roshanadh.orderservice.model.OrderLineItems;
import np.roshanadh.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

  private final OrderRepository orderRepository;
  private final WebClient webClient;

  public void placeOrder(OrderRequest orderRequest) {
    Order order = new Order();
    order.setOrderNumber(UUID.randomUUID().toString());

    var orderLineItems = orderRequest.getOrderLineItemsDtoList()
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());

    order.setOrderLineItemsList(orderLineItems);

    var skuCodes = order.getOrderLineItemsList()
            .stream()
            .map(OrderLineItems::getSkuCode)
            .collect(Collectors.toList());

    // Call Inventory Service, and place order if product is in stock
    String inventoryServiceUri = "http://localhost:8082/api/inventory";
    InventoryResponse[] inventoryResponses = webClient.get()
            .uri(inventoryServiceUri,
                    uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
            .retrieve()
            .bodyToMono(InventoryResponse[].class)
            .block();

    boolean allProductsInStock = Arrays.stream(inventoryResponses)
            .allMatch(InventoryResponse::isInStock);

    if (allProductsInStock) orderRepository.save(order);
    else throw new IllegalArgumentException("Order out of stock");
  }

  private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
    OrderLineItems orderLineItems = new OrderLineItems();
    orderLineItems.setPrice(orderLineItemsDto.getPrice());
    orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
    orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
    return orderLineItems;
  }
}
