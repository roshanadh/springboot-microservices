package np.roshanadh.orderservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import np.roshanadh.orderservice.dto.InventoryResponse;
import np.roshanadh.orderservice.dto.OrderLineItemsDto;
import np.roshanadh.orderservice.dto.OrderRequest;
import np.roshanadh.orderservice.model.Order;
import np.roshanadh.orderservice.model.OrderLineItems;
import np.roshanadh.orderservice.repository.OrderRepository;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

  private final OrderRepository orderRepository;
  private final WebClient.Builder webClientBuilder;
  private final Tracer tracer;

  public String placeOrder(OrderRequest orderRequest) {
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

    log.info("Calling inventory service");
    // create a Span ID
    Span inventoryServiceLookup = tracer
            .nextSpan()
            // give a unique name to this particular span
            .name("inventoryServiceLookup");
    // execute the placeOrder logic under the just-created span ID
    try (Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())) {
      // Call Inventory Service, and place order if product is in stock
      String inventoryServiceUri = "http://inventory-service/api/inventory";
      InventoryResponse[] inventoryResponses = webClientBuilder.build()
              .get()
              .uri(inventoryServiceUri,
                      uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
              .retrieve()
              .bodyToMono(InventoryResponse[].class)
              .block();

      boolean allProductsInStock = Arrays.stream(inventoryResponses)
              .allMatch(InventoryResponse::isInStock);

      if (allProductsInStock) {
        orderRepository.save(order);
        return "Order placed successfully!";
      } else throw new IllegalArgumentException("Order out of stock");
    } finally {
      inventoryServiceLookup.end();
    }


  }

  private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
    OrderLineItems orderLineItems = new OrderLineItems();
    orderLineItems.setPrice(orderLineItemsDto.getPrice());
    orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
    orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
    return orderLineItems;
  }
}
