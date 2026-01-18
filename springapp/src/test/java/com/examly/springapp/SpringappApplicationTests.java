package com.examly.springapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpringappApplicationTests {

    private String usertoken;
    private String admintoken;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper; // To parse JSON responses

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    void backend_testAllFilesExist() {

        // List of expected files in the 'controller' folder
        String[] controllerFiles = {
                "src/main/java/com/examly/springapp/controller/FeedbackController.java",
                "src/main/java/com/examly/springapp/controller/OrderController.java",
                "src/main/java/com/examly/springapp/controller/ProductController.java",
                "src/main/java/com/examly/springapp/controller/UserController.java"
        };

        // List of expected files in the 'model' folder
        String[] modelFiles = {
                "src/main/java/com/examly/springapp/model/Feedback.java",
                "src/main/java/com/examly/springapp/model/LoginDTO.java",
                "src/main/java/com/examly/springapp/model/Order.java",
                "src/main/java/com/examly/springapp/model/Product.java",
                "src/main/java/com/examly/springapp/model/User.java"
        };

        // List of expected files in the 'repository' folder
        String[] repositoryFiles = {
                "src/main/java/com/examly/springapp/repository/FeedbackRepo.java",
                "src/main/java/com/examly/springapp/repository/OrderRepo.java",
                "src/main/java/com/examly/springapp/repository/ProductRepo.java",
                "src/main/java/com/examly/springapp/repository/UserRepo.java"
        };

        // List of expected files in the 'service' folder
        String[] serviceFiles = {
                "src/main/java/com/examly/springapp/service/FeedbackService.java",
                "src/main/java/com/examly/springapp/service/FeedbackServiceImpl.java",
                "src/main/java/com/examly/springapp/service/OrderService.java",
                "src/main/java/com/examly/springapp/service/OrderServiceImpl.java",
                "src/main/java/com/examly/springapp/service/ProductService.java",
                "src/main/java/com/examly/springapp/service/ProductServiceImpl.java",
                "src/main/java/com/examly/springapp/service/UserService.java",
                "src/main/java/com/examly/springapp/service/UserServiceImpl.java"
        };

        // Verify Controller files
        for (String filePath : controllerFiles) {
            File file = new File(filePath);
            assertTrue(file.exists() && file.isFile(), "File not found: " + filePath);
        }

        // Verify Model files
        for (String filePath : modelFiles) {
            File file = new File(filePath);
            assertTrue(file.exists() && file.isFile(), "File not found: " + filePath);
        }

        // Verify Repository files
        for (String filePath : repositoryFiles) {
            File file = new File(filePath);
            assertTrue(file.exists() && file.isFile(), "File not found: " + filePath);
        }

        // Verify Service files
        for (String filePath : serviceFiles) {
            File file = new File(filePath);
            assertTrue(file.exists() && file.isFile(), "File not found: " + filePath);
        }
    }

    @Test
    void backend_testAllFoldersExist() {
        String[] expectedFolders = {
                "src/main/java/com/examly/springapp/controller",
                "src/main/java/com/examly/springapp/model",
                "src/main/java/com/examly/springapp/repository",
                "src/main/java/com/examly/springapp/service"
        };

        for (String folderPath : expectedFolders) {
            File folder = new File(folderPath);
            assertTrue(folder.exists() && folder.isDirectory(), "Folder not found: " + folderPath);
        }
    }

    @Test
    @Order(1)
    void backend_testRegisterAdmin() {
        String requestBody = "{\"userId\": 1,\"email\": \"demoadmin@gmail.com\", \"password\": \"admin@1234\", \"username\": \"admin123\", \"userRole\": \"ADMIN\", \"mobileNumber\": \"9876543210\"}";
        ResponseEntity<String> response = restTemplate.postForEntity("/api/register",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(2)
    void backend_testRegisterUser() {
        String requestBody = "{\"userId\": 2,\"email\": \"demouser@gmail.com\", \"password\": \"user@1234\", \"username\": \"user123\", \"userRole\": \"USER\", \"mobileNumber\": \"1122334455\"}";
        ResponseEntity<String> response = restTemplate.postForEntity("/api/register",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @Order(3)
    void backend_testLoginAdmin() throws Exception {
        String requestBody = "{\"username\": \"admin123\", \"password\": \"admin@1234\"}";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/login",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        // Check if response body is null
        Assertions.assertNotNull(response.getBody(), "Response body is null!");

        JsonNode responseBody = objectMapper.readTree(response.getBody());
        String token = responseBody.get("token").asText();
        admintoken = token;

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(token);
    }

    @Test
    @Order(4)
    void backend_testLoginUser() throws Exception {
        String requestBody = "{\"username\": \"user123\", \"password\": \"user@1234\"}";

        ResponseEntity<String> response = restTemplate.postForEntity("/api/login",
                new HttpEntity<>(requestBody, createHeaders()), String.class);

        JsonNode responseBody = objectMapper.readTree(response.getBody());
        String token = responseBody.get("token").asText();
        usertoken = token;

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(token);
    }

    @Test
    @Order(5)
    void backend_testGetAllUsersByAdminWithRoleValidation() throws Exception {
        // Ensure tokens are available
        Assertions.assertNotNull(admintoken, "Admin token should not be null");
        Assertions.assertNotNull(usertoken, "User token should not be null");

        String url = "/api/user";

        // Test with Admin token (Expecting 200 OK and list of users)
        HttpHeaders adminHeaders = createHeaders();
        adminHeaders.set("Authorization", "Bearer " + admintoken);
        HttpEntity<Void> adminRequest = new HttpEntity<>(adminHeaders);

        ResponseEntity<String> adminResponse = restTemplate.exchange(url, HttpMethod.GET, adminRequest, String.class);

        System.out.println(adminResponse.getStatusCode() + " Status code for Admin retrieving user list");
        Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());

        // Parse and validate JSON response (Expecting a list of users)
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseBody = objectMapper.readTree(adminResponse.getBody());

        // Verify that the response is an array
        Assertions.assertTrue(responseBody.isArray(), "Response should be a JSON array");

        // Check if the first user (ADMIN) has expected fields
        JsonNode firstUser = responseBody.get(0);
        Assertions.assertEquals(1, firstUser.get("userId").asInt());
        Assertions.assertEquals("demoadmin@gmail.com", firstUser.get("email").asText());
        Assertions.assertEquals("admin123", firstUser.get("username").asText());
        Assertions.assertEquals("ADMIN", firstUser.get("userRole").asText());
        Assertions.assertEquals("9876543210", firstUser.get("mobileNumber").asText());

        // Check if the second user (USER) has expected fields
        JsonNode secondUser = responseBody.get(1);
        Assertions.assertEquals(2, secondUser.get("userId").asInt());
        Assertions.assertEquals("demouser@gmail.com", secondUser.get("email").asText());
        Assertions.assertEquals("user123", secondUser.get("username").asText());
        Assertions.assertEquals("USER", secondUser.get("userRole").asText());
        Assertions.assertEquals("1122334455", secondUser.get("mobileNumber").asText());

        System.out.println("Admin Response JSON: " + responseBody.toString());

        // Test with User token (Expecting 403 Forbidden)
        HttpHeaders userHeaders = createHeaders();
        userHeaders.set("Authorization", "Bearer " + usertoken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<String> userResponse = restTemplate.exchange(url, HttpMethod.GET, userRequest, String.class);

        System.out.println(userResponse.getStatusCode() + " Status code for User trying to retrieve user list");
        Assertions.assertEquals(HttpStatus.FORBIDDEN, userResponse.getStatusCode());
    }

    @Test
    @Order(6)
    void backend_testAddProductWithRoleValidation() throws Exception {
        // Ensure tokens are available
        Assertions.assertNotNull(admintoken, "Admin token should not be null");
        Assertions.assertNotNull(usertoken, "User token should not be null");

        // Construct the Request Body for Product WITHOUT WRAPPER
        String requestBody = "{"
                + "\"name\": \"Laptop\","
                + "\"description\": \"Gaming Laptop\","
                + "\"price\": " + 85000.00 + ","
                + "\"stock\": " + 10 + ","
                + "\"category\": \"Electronics\","
                + "\"photoImage\": \"photoimageurl\","
                + "\"user\": {\"userId\": 1}"
                + "}";

        // Test with Admin Token (Expecting 201 Created)
        HttpHeaders adminHeaders = createHeaders();
        adminHeaders.set("Authorization", "Bearer " + admintoken);
        HttpEntity<String> adminRequest = new HttpEntity<>(requestBody, adminHeaders);

        // Hit the API with Admin Token
        ResponseEntity<String> adminResponse = restTemplate.exchange("/api/products", HttpMethod.POST, adminRequest,
                String.class);

        // Verify Response Status for Admin (201 Created)
        System.out.println(adminResponse.getStatusCode() + " Status code for Admin adding product");
        Assertions.assertEquals(HttpStatus.CREATED, adminResponse.getStatusCode());

        // Parse the Response Body
        JsonNode responseBody = objectMapper.readTree(adminResponse.getBody());

        // Verify Product Fields in Response
        Assertions.assertEquals(1, responseBody.get("productId").asInt());
        Assertions.assertEquals("Laptop", responseBody.get("name").asText());
        Assertions.assertEquals("Gaming Laptop", responseBody.get("description").asText());
        Assertions.assertEquals(85000.00, responseBody.get("price").asDouble());
        Assertions.assertEquals(10, responseBody.get("stock").asInt());
        Assertions.assertEquals("Electronics", responseBody.get("category").asText());

        // Verify Image Is Stored In Byte[] Format
        Assertions.assertNotNull(responseBody.get("photoImage").asText());
        System.out.println("Admin Response JSON: " + responseBody.toString());

        // Test with User Token (Expecting 403 Forbidden)
        HttpHeaders userHeaders = createHeaders();
        userHeaders.set("Authorization", "Bearer " + usertoken);
        HttpEntity<String> userRequest = new HttpEntity<>(requestBody, userHeaders);

        // Hit the API with User Token
        ResponseEntity<String> userResponse = restTemplate.exchange("/api/products", HttpMethod.POST, userRequest,
                String.class);

        // Verify Response Status for User (403 Forbidden)
        System.out.println(userResponse.getStatusCode() + " Status code for User trying to add product");
        Assertions.assertEquals(HttpStatus.FORBIDDEN, userResponse.getStatusCode());
    }

    @Test
    @Order(7)
    void backend_testGetProductByIdWithRoleValidation() throws Exception {
        // Ensure tokens are available
        Assertions.assertNotNull(admintoken, "Admin token should not be null");
        Assertions.assertNotNull(usertoken, "User token should not be null");

        // Test with Admin token (Expecting 200 OK)
        HttpHeaders adminHeaders = createHeaders();
        adminHeaders.set("Authorization", "Bearer " + admintoken);
        HttpEntity<Void> adminRequest = new HttpEntity<>(adminHeaders);

        ResponseEntity<String> adminResponse = restTemplate.exchange("/api/products/1", HttpMethod.GET, adminRequest,
                String.class);

        // Verify Response Status for Admin (200 OK)
        Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());

        // Test with User token (Expecting 200 OK)
        HttpHeaders userHeaders = createHeaders();
        userHeaders.set("Authorization", "Bearer " + usertoken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<String> userResponse = restTemplate.exchange("/api/products/1", HttpMethod.GET, userRequest,
                String.class);

        // Verify Response Status for User (200 OK)
        Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode());
    }

    @Test
    @Order(8)
    void backend_testGetProductByUserIdWithRoleValidation() throws Exception {

        Assertions.assertNotNull(admintoken, "Admin token should not be null");
        Assertions.assertNotNull(usertoken, "User token should not be null");

        String url = "/api/products/user/1";

        HttpHeaders adminHeaders = createHeaders();
        adminHeaders.set("Authorization", "Bearer " + admintoken);
        HttpEntity<Void> adminRequest = new HttpEntity<>(adminHeaders);

        ResponseEntity<String> adminResponse = restTemplate.exchange(url, HttpMethod.GET, adminRequest, String.class);

        // Verify Response Status for Admin (200 OK)
        Assertions.assertEquals(HttpStatus.FORBIDDEN, adminResponse.getStatusCode());

        // Test with User token (Expecting 403 Forbidden)
        HttpHeaders userHeaders = createHeaders();
        userHeaders.set("Authorization", "Bearer " + usertoken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<String> userResponse = restTemplate.exchange(url, HttpMethod.GET, userRequest, String.class);

        // Verify Response Status for User (403 Forbidden)
        Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode());
    }

    @Test
    @Order(9)
    void backend_testGetAllProductsWithRoleValidation() throws Exception {
        // Ensure tokens are available
        Assertions.assertNotNull(admintoken, "Admin token should not be null");
        Assertions.assertNotNull(usertoken, "User token should not be null");

        String url = "/api/products";

        // Test with Admin token (Expecting 200 OK)
        HttpHeaders adminHeaders = createHeaders();
        adminHeaders.set("Authorization", "Bearer " + admintoken);
        HttpEntity<Void> adminRequest = new HttpEntity<>(adminHeaders);

        ResponseEntity<String> adminResponse = restTemplate.exchange(url, HttpMethod.GET, adminRequest, String.class);
        Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());

        // Test with User token (Expecting 200 OK)
        HttpHeaders userHeaders = createHeaders();
        userHeaders.set("Authorization", "Bearer " + usertoken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<String> userResponse = restTemplate.exchange(url, HttpMethod.GET, userRequest, String.class);
        Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode());
    }

    @Test
    @Order(10)
    void backend_testUpdateProductWithRoleValidation() throws Exception {

        Assertions.assertNotNull(admintoken, "Admin token should not be null");
        Assertions.assertNotNull(usertoken, "User token should not be null");

        String requestBody = "{"
                + "\"name\": \"Gaming Laptop\","
                + "\"description\": \"High-end gaming laptop\","
                + "\"price\": " + 95000.00 + ","
                + "\"stock\": " + 5 + ","
                + "\"category\": \"Electronics\","
                + "\"photoImage\": \"updatedphotoimageurl\","
                + "\"user\": {\"userId\": 1}"
                + "}";

        // Test with Admin Token (Expecting 200 OK)
        HttpHeaders adminHeaders = createHeaders();
        adminHeaders.set("Authorization", "Bearer " + admintoken);
        HttpEntity<String> adminRequest = new HttpEntity<>(requestBody, adminHeaders);

        // Hit the Update API with Admin Token
        ResponseEntity<String> adminResponse = restTemplate.exchange("/api/products/1", HttpMethod.PUT, adminRequest,
                String.class);

        // Verify Response Status for Admin (200 OK)
        Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode());

        // Verify Product Fields in Response
        JsonNode responseBody = objectMapper.readTree(adminResponse.getBody());
        Assertions.assertEquals(1, responseBody.get("productId").asInt());
        Assertions.assertEquals("Gaming Laptop", responseBody.get("name").asText());
        Assertions.assertEquals("High-end gaming laptop", responseBody.get("description").asText());
        Assertions.assertEquals(95000.00, responseBody.get("price").asDouble());
        Assertions.assertEquals(5, responseBody.get("stock").asInt());
        Assertions.assertEquals("Electronics", responseBody.get("category").asText());

        // Test with User Token (Expecting 403 Forbidden)
        HttpHeaders userHeaders = createHeaders();
        userHeaders.set("Authorization", "Bearer " + usertoken);
        HttpEntity<String> userRequest = new HttpEntity<>(requestBody, userHeaders);

        // Hit the API with User Token
        ResponseEntity<String> userResponse = restTemplate.exchange("/api/products/1", HttpMethod.PUT, userRequest,
                String.class);

        // Verify Response Status for User (403 Forbidden)
        Assertions.assertEquals(HttpStatus.FORBIDDEN, userResponse.getStatusCode());
    }

    @Test
    @Order(11)
    void backend_testAddOrder() throws Exception {
        Assertions.assertNotNull(usertoken, "User token should not be null");
        Assertions.assertNotNull(admintoken, "Admin token should not be null");

        String requestBody = "{" +
                "\"user\": {\"userId\": 2}," +
                "\"product\": [{\"productId\": 1}]," +
                "\"shippingAddress\": \"123 Main St, City\"," +
                "\"totalAmount\": 85000.00," +
                "\"quantity\": 1," +
                "\"status\": \"PENDING\"" +
                "}";

        HttpHeaders userHeaders = createHeaders();
        userHeaders.set("Authorization", "Bearer " + usertoken);
        HttpEntity<String> userRequest = new HttpEntity<>(requestBody, userHeaders);

        ResponseEntity<String> userResponse = restTemplate.exchange("/api/orders", HttpMethod.POST, userRequest,
                String.class);
        Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode(), "User should be able to add an order");

        HttpHeaders adminHeaders = createHeaders();
        adminHeaders.set("Authorization", "Bearer " + admintoken);
        HttpEntity<String> adminRequest = new HttpEntity<>(requestBody, adminHeaders);

        ResponseEntity<String> adminResponse = restTemplate.exchange("/api/orders", HttpMethod.POST, adminRequest,
                String.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, adminResponse.getStatusCode(),
                "Admin should NOT be able to add an order");
    }

    @Test
    @Order(12)
    void backend_testGetOrderById() throws Exception {
        Assertions.assertNotNull(usertoken, "User token should not be null");

        HttpHeaders headers = createHeaders();
        headers.set("Authorization", "Bearer " + usertoken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/orders/1", HttpMethod.GET, request, String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(13)
    void backend_testGetAllOrders() throws Exception {
        Assertions.assertNotNull(usertoken, "User token should not be null");
        Assertions.assertNotNull(admintoken, "Admin token should not be null");
 
        HttpHeaders headers = createHeaders();
        headers.set("Authorization", "Bearer " + admintoken);
        HttpEntity<Void> adminRequest = new HttpEntity<>(headers);

        ResponseEntity<String> adminResponse = restTemplate.exchange("/api/orders", HttpMethod.GET, adminRequest,
                String.class);
        Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode(), "Admin should be able to get all orders");

        HttpHeaders userHeaders = createHeaders();
        userHeaders.set("Authorization", "Bearer " + usertoken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<String> userResponse = restTemplate.exchange("/api/orders", HttpMethod.GET, userRequest,
                String.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, userResponse.getStatusCode(),
                "User should NOT be able to get all orders");
    }

    @Test
    @Order(14)
    void backend_testGetOrdersByUserId() throws Exception {
        Assertions.assertNotNull(usertoken, "User token should not be null");
        Assertions.assertNotNull(admintoken, "Admin token should not be null");

        HttpHeaders headers = createHeaders();

        headers.set("Authorization", "Bearer " + usertoken);
        HttpEntity<Void> userRequest = new HttpEntity<>(headers);
        ResponseEntity<String> userResponse = restTemplate.exchange("/api/orders/user/2", HttpMethod.GET, userRequest,
                String.class);
        Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode(),
                "User should be able to get orders by userId");

        headers.set("Authorization", "Bearer " + admintoken);
        HttpEntity<Void> adminRequest = new HttpEntity<>(headers);
        ResponseEntity<String> adminResponse = restTemplate.exchange("/api/orders/user/2", HttpMethod.GET, adminRequest,
                String.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, adminResponse.getStatusCode(),
                "Admin should NOT be able to get orders by userId");
    }

    @Test
    @Order(15)
    void backend_testUpdateOrder() throws Exception {
        Assertions.assertNotNull(usertoken, "User token should not be null");
        Assertions.assertNotNull(admintoken, "Admin token should not be null");

        String requestBody = "{" +
                "\"shippingAddress\": \"456 Updated St, New City\"," +
                "\"totalAmount\": 90000.00," +
                "\"quantity\": 2," +
                "\"status\": \"SHIPPED\"" +
                "}";

        HttpHeaders headers = createHeaders();

        headers.set("Authorization", "Bearer " + usertoken);
        HttpEntity<String> userRequest = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> userResponse = restTemplate.exchange("/api/orders/1", HttpMethod.PUT, userRequest,
                String.class);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, userResponse.getStatusCode(), "User should be able to update an order");

        headers.set("Authorization", "Bearer " + admintoken);
        HttpEntity<String> adminRequest = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> adminResponse = restTemplate.exchange("/api/orders/1", HttpMethod.PUT, adminRequest,
                String.class);
        Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode(),
                "Admin should be able to update an order");
    }

    @Test
@Order(16)
void backend_testAddFeedback() throws Exception {
    Assertions.assertNotNull(usertoken, "User token should not be null");
    Assertions.assertNotNull(admintoken, "Admin token should not be null");

    String requestBody = "{" +
            "\"user\": {\"userId\": 2}," +
            "\"message\": \"Great service!\", " +
            "\"rating\": 5" +
            "}";

    //  User should be able to add feedback
    HttpHeaders userHeaders = createHeaders();
    userHeaders.set("Authorization", "Bearer " + usertoken);
    HttpEntity<String> userRequest = new HttpEntity<>(requestBody, userHeaders);
    ResponseEntity<String> userResponse = restTemplate.exchange("/api/feedback", HttpMethod.POST, userRequest, String.class);
    Assertions.assertEquals(HttpStatus.CREATED, userResponse.getStatusCode(), "User should be able to add feedback");

    //  Admin should NOT be able to add feedback
    HttpHeaders adminHeaders = createHeaders();
    adminHeaders.set("Authorization", "Bearer " + admintoken);
    HttpEntity<String> adminRequest = new HttpEntity<>(requestBody, adminHeaders);
    ResponseEntity<String> adminResponse = restTemplate.exchange("/api/feedback", HttpMethod.POST, adminRequest, String.class);
    Assertions.assertEquals(HttpStatus.FORBIDDEN, adminResponse.getStatusCode(), "Admin should NOT be able to add feedback");
}

@Test
@Order(17)
void backend_testGetAllFeedback() throws Exception {
    Assertions.assertNotNull(usertoken, "User token should not be null");
    Assertions.assertNotNull(admintoken, "Admin token should not be null");

    //  Admin should be able to get all feedback
    HttpHeaders adminHeaders = createHeaders();
    adminHeaders.set("Authorization", "Bearer " + admintoken);
    HttpEntity<Void> adminRequest = new HttpEntity<>(adminHeaders);
    ResponseEntity<String> adminResponse = restTemplate.exchange("/api/feedback", HttpMethod.GET, adminRequest, String.class);
    Assertions.assertEquals(HttpStatus.OK, adminResponse.getStatusCode(), "Admin should be able to view all feedback");

    //  User should NOT be able to get all feedback
    HttpHeaders userHeaders = createHeaders();
    userHeaders.set("Authorization", "Bearer " + usertoken);
    HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);
    ResponseEntity<String> userResponse = restTemplate.exchange("/api/feedback", HttpMethod.GET, userRequest, String.class);
    Assertions.assertEquals(HttpStatus.FORBIDDEN, userResponse.getStatusCode(), "User should NOT be able to view all feedback");
}

@Test
@Order(18)
void backend_testGetFeedbackByUserId() throws Exception {
    Assertions.assertNotNull(usertoken, "User token should not be null");
    Assertions.assertNotNull(admintoken, "Admin token should not be null");

    //  User should be able to get their own feedback
    HttpHeaders userHeaders = createHeaders();
    userHeaders.set("Authorization", "Bearer " + usertoken);
    HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);
    ResponseEntity<String> userResponse = restTemplate.exchange("/api/feedback/user/2", HttpMethod.GET, userRequest, String.class);
    Assertions.assertEquals(HttpStatus.OK, userResponse.getStatusCode(), "User should be able to get their own feedback");

    //  Admin should NOT be able to get user feedback
    HttpHeaders adminHeaders = createHeaders();
    adminHeaders.set("Authorization", "Bearer " + admintoken);
    HttpEntity<Void> adminRequest = new HttpEntity<>(adminHeaders);
    ResponseEntity<String> adminResponse = restTemplate.exchange("/api/feedback/user/2", HttpMethod.GET, adminRequest, String.class);
    Assertions.assertEquals(HttpStatus.FORBIDDEN, adminResponse.getStatusCode(), "Admin should NOT be able to get user feedback");
} 

}