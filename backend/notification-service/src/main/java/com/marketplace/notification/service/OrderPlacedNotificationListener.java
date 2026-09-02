package com.marketplace.notification.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.marketplace.notification.dto.OrderPlacedNotificationEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderPlacedNotificationListener {

  private final EmailService emailService;

  @KafkaListener(topics = "${app.kafka.topics.order-placed-notification}")
  public void onOrderPlaced(OrderPlacedNotificationEvent event) {
    emailService.send(event.customerEmail(),
        "Order confirmation - " + event.orderId(),
        buildEmailBody(event));
    log.info("Order confirmation email sent for order {} to {}", event.orderId(), event.customerEmail());
  }

  private String buildEmailBody(OrderPlacedNotificationEvent event) {
    StringBuilder builder = new StringBuilder();
    builder.append("Dear ")
        .append(event.customerName())
        .append(",\n\n")
        .append("Thanks for shopping with us. Your order ")
        .append(event.orderId())
        .append(" is confirmed.\n\n")
        .append("Products ordered:\n");

    event.products().forEach(product -> builder
        .append("- Product ")
        .append(product.productId())
        .append(" | Quantity: ")
        .append(product.quantity())
        .append(" | Price: ")
        .append(product.price())
        .append("\n"));

    builder.append("\nWe will notify you again when your order is shipped.\n\n")
        .append("Regards,\nMarketplace Team");

    return builder.toString();
  }
}
