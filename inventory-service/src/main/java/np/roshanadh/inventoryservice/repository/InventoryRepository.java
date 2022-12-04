package np.roshanadh.inventoryservice.repository;

import np.roshanadh.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
  List<Inventory> findBySkuCodeIn(List<String> skuCodeList);
}
