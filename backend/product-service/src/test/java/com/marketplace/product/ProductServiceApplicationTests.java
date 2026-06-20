package com.marketplace.product;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.mongodb.MongoDBContainer;

import io.restassured.RestAssured;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

  @ServiceConnection
  static MongoDBContainer mongoDBContainer =
          new MongoDBContainer("mongo:7.0.5");

  @LocalServerPort
  private int port;

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

  static {
    mongoDBContainer.start();
  }

  @Test
  void shouldCreateProduct() {
    String requestBody = """
{
    "name": "integration test",
    "description": "a test from test container",
    "price": 0,
    "imgUrl": "www.demourl"
}
            """;

    RestAssured.given()
            .contentType("application/json")
            .body(requestBody)
            .when().post("/api/product/create")
            .then()
            .statusCode(201)
            .body("id", Matchers.notNullValue())
            .body("name", Matchers.equalTo("integration test"))
            .body("description", Matchers.equalTo("a test from test container"))
            .body("price", Matchers.equalTo(0));
  }
}