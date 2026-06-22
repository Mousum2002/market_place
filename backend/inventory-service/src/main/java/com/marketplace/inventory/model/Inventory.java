package com.marketplace.inventory.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import java.util.UUID;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
public class Inventory {
    @Id
    private UUID id;
    @Column(name = "product_id")
    private UUID productId;
    private Integer quantity;
}
