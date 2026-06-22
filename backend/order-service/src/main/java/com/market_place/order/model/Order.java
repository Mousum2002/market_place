package com.market_place.order.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
public class Order {

  @Id
  @Column(columnDefinition = "uuid")
  private UUID id;
  @Column(columnDefinition = "uuid", name = "customer_id", nullable = false)
  private UUID customerId;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "products", columnDefinition = "jsonb", nullable = false)
  private List<OrderProduct> products = new ArrayList<>();

  @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
  private BigDecimal totalPrice;

  @Column(name = "created_at")
  private LocalDateTime createdAt;
  //
  //
  // id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  // customer_id UUID NOT NULL,
  // products JSONB NOT NULL,
  // total_price NUMERIC(10,2) NOT NULL,
  // created_at TIMESTAMP DEFAULT NOW()

}
