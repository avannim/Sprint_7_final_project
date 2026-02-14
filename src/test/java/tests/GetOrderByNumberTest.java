package tests;

import com.google.gson.Gson;
import dto.request.CreateOrderRequest;
import dto.request.LoginRequest;
import dto.response.CreateOrderResponse;
import dto.response.ErrorResponse;
import dto.response.GetOrderByNumberResponse;
import dto.response.LoginCourierResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import steps.ServiceSteps;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetOrderByNumberTest {

    private final ServiceSteps serviceSteps = new ServiceSteps();
    private final Gson gson = new Gson();
    CreateOrderResponse createOrderResponse;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        CreateOrderRequest createOrderRequest = new CreateOrderRequest("Елена", "Журчалко", "г. Москва, проспект Жуковского, д.123", 5, "+79203958642", 6, serviceSteps.createDate(3),"Получать будет Марина", List.of("BLACK"));
        String body = gson.toJson(createOrderRequest);
        System.out.println(body);
        Response response = serviceSteps.sendPostRequest("/api/v1/orders", body);
        serviceSteps.checkRequestStatus(response, 201);
        createOrderResponse = gson.fromJson(response.body().asString(), CreateOrderResponse.class);
    }

     @Test
    @DisplayName("Проверка успешного получения заказа по его номеру")
    public void checkGetOrderByNumber(){
         Response response = serviceSteps.sendGetRequestWithParam("/api/v1/orders/track", "t", createOrderResponse.getTrack());
         serviceSteps.checkRequestStatus(response, 200);
         GetOrderByNumberResponse orderByNumber = gson.fromJson(response.body().asString(),GetOrderByNumberResponse.class);
         assertNotNull(orderByNumber);
     }

    @Test
    @DisplayName("Проверка успешного получения заказа по несуществующему номеру")
    public void checkGetOrderByWrongNumber(){
        Response response = serviceSteps.sendGetRequestWithParam("/api/v1/orders/track", "t", createOrderResponse.getTrack()+2);
        serviceSteps.checkRequestStatus(response, 404);
        ErrorResponse errorResponse = gson.fromJson(response.body().asString(), ErrorResponse.class);
        serviceSteps.checkRequestMessage("Заказ не найден", errorResponse.getMessage());
    }

    @Test
    @DisplayName("Проверка успешного получения заказа без указания номера")
    public void checkGetOrderWithoutNumber(){
        Response response = serviceSteps.sendGetRequest("/api/v1/orders/track");
        serviceSteps.checkRequestStatus(response, 400);
        ErrorResponse errorResponse = gson.fromJson(response.body().asString(), ErrorResponse.class);
        serviceSteps.checkRequestMessage("Недостаточно данных для поиска", errorResponse.getMessage());
    }

    @AfterEach
    public void tearDown(TestInfo testInfo) {
        String body = gson.toJson(createOrderResponse);
        serviceSteps.sendPutRequest("/api/v1/orders/cancel", body);
    }
}
