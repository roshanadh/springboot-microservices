package np.roshanadh.inventoryservice;

import lombok.extern.slf4j.Slf4j;
import np.roshanadh.inventoryservice.model.Inventory;
import np.roshanadh.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
@EnableEurekaClient
public class InventoryServiceApplication {

	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
		return args -> {
			Inventory inventory = new Inventory();
			inventory.setSkuCode("iphone_13");
			inventory.setQuantity(100);

			Inventory inventory1 = new Inventory();
			inventory1.setSkuCode("iphone_13_red");
			inventory1.setQuantity(0);

			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);

			log.info("Added to inventory {} and {}", inventory, inventory1);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

}
