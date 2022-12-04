package np.roshanadh.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import np.roshanadh.inventoryservice.dto.InventoryResponse;
import np.roshanadh.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

  private final InventoryRepository inventoryRepository;

  @Transactional(readOnly = true)
  public List<InventoryResponse> isInStock(List<String> skuCodeList) {
    return inventoryRepository.findBySkuCodeIn(skuCodeList)
            .stream()
            .map(inventory -> 
              InventoryResponse.builder()
                      .skuCode(inventory.getSkuCode())
                      .isInStock(inventory.getQuantity() > 0)
                      .build()
            )
            .collect(Collectors.toList());
  }
}
