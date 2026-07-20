package com.marketplace.gateway.routes;

import java.util.function.Function;

import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class Routes {

  private final JwtDecoder jwtDecoder;

  @Bean
  public RouterFunction<ServerResponse> productServiceRoute() {
    return GatewayRouterFunctions.route("product_service")
        .route(RequestPredicates.path("/api/product/**"), HandlerFunctions.http())
        .before(BeforeFilterFunctions.uri("http://localhost:8080")).build();
  }

  @Bean
  public RouterFunction<ServerResponse> orderGetRoute() {
    return GatewayRouterFunctions.route("order_my_orders")
        .route(RequestPredicates.GET("/api/order/my-orders"), HandlerFunctions.http())
        .before(addUserIdHeader())
        .before(BeforeFilterFunctions.uri("http://localhost:8081"))
        .build();
  }

  @Bean
  public RouterFunction<ServerResponse> orderPostRoute() {
    return GatewayRouterFunctions.route("order_place")
        .route(RequestPredicates.POST("/api/order"), HandlerFunctions.http())
        .before(addUserIdHeader())
        .before(BeforeFilterFunctions.uri("http://localhost:8081"))
        .build();
  }

  @Bean
  public RouterFunction<ServerResponse> authServiceRoute() {
    return GatewayRouterFunctions.route("auth_service")
        .route(RequestPredicates.path("/api/auth/**"), HandlerFunctions.http())
        .before(BeforeFilterFunctions.uri("http://localhost:8282")).build();
  }

  private Function<ServerRequest, ServerRequest> addUserIdHeader() {
    return request -> {
      String authHeader = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);

      if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return request;
      }

      String token = authHeader.substring(7);
      Jwt jwt = jwtDecoder.decode(token);
      String userId = jwt.getClaimAsString("userId");

      if (userId == null || userId.isBlank()) {
        return request;
      }

      log.info("Forwarding X-Customer-Id: {}", userId);

      return ServerRequest.from(request)
          .header("X-Customer-Id", userId)
          .build();
    };
  }
}
