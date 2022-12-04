package np.roshanadh.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "t_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String orderNumber;
  @OneToMany(cascade = CascadeType.ALL)
  private List<OrderLineItems> orderLineItemsList = new java.util.ArrayList<>();
}
