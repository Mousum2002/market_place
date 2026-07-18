package com.marketplace.inventory.repo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.marketplace.inventory.dto.ProductQuantityRequest;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InventoryBulkRepo {

  private final JdbcTemplate jdbcTemplate;

  public Set<UUID> subtractIfAvailable(List<ProductQuantityRequest> products) {

    String valueClause = IntStream.range(0, products.size())
        .mapToObj(i -> "(?::uuid, ?::int)")
        .collect(Collectors.joining(", "));

    String sql = """
        UPDATE inventory i
            SET quantity = i.quantity - v.requested_qty
            FROM (
                VALUES %s
            ) AS v(product_id, requested_qty)
            WHERE i.product_id = v.product_id
              AND i.quantity >= v.requested_qty
            RETURNING i.product_id
            """.formatted(valueClause);

    List<Object> params = new ArrayList<>();
    for (ProductQuantityRequest product : products) {
      params.add(product.productID());
      params.add(product.quantity());

    }
    return new HashSet<>(
        jdbcTemplate.query(
            sql,
            ps -> {
              for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
              }
            },
            (rs, rowNum) -> rs.getObject("product_id", UUID.class)));

  }
}
