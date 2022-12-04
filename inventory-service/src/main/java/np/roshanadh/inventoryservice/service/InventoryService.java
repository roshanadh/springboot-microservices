package np.roshanadh.inventoryservice.service;

import lombok.RequiredArgsConstructor;
import np.roshanadh.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

  private final InventoryRepository inventoryRepository;

  @Transactional(readOnly = true)
  public boolean isInStock(String skuCode) {
    return inventoryRepository.findBySkuCode(skuCode).isPresent();
  }
}
