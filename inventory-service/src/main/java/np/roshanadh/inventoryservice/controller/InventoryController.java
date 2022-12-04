package np.roshanadh.inventoryservice.controller;

import lombok.RequiredArgsConstructor;
import np.roshanadh.inventoryservice.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

  private final InventoryService inventoryService;

  @GetMapping("/{sku-code}")
  @ResponseStatus(HttpStatus.OK)
  public boolean isInStock(@PathVariable("sku-code") String skuCode) {
    return inventoryService.isInStock(skuCode);
  }
}
