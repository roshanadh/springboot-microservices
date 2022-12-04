package me.roshanadh.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductResponse {
  private String id;
  private String name;
  private String description;
  private BigDecimal price;
}
