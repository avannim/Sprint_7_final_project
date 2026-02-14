package tests;

import com.google.gson.Gson;
import dto.request.CreateCourierRequest;
import dto.request.CreateOrderRequest;
import dto.request.LoginRequest;
import dto.response.CreateOrderResponse;
import dto.response.ErrorResponse;
import dto.response.LoginCourierResponse;
import dto.response.OkResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import steps.ServiceSteps;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TakeOrderTest {

    private final ServiceSteps serviceSteps = new ServiceSteps();
    private final Gson gson = new Gson();
    private CreateOrderResponse createOrderResponse;
    private LoginCourierResponse loginResponse;
    private Response response;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        CreateOrderRequest createOrderRequest = new CreateOrderRequest("Елена", "Журчалко", "г. Москва, проспект Жуковского, д.123", 5, "+79203958642", 6, serviceSteps.createDate(3),"Получать будет Марина", List.of("BLACK"));
        String body = gson.toJson(createOrderRequest);
        response = serviceSteps.sendPostRequest("/api/v1/orders", body);
        serviceSteps.checkRequestStatus(response, 201);
        createOrderResponse = gson.fromJson(response.body().asString(), CreateOrderResponse.class);
        CreateCourierRequest newCourier = new CreateCourierRequest("oddinson_courier", "Qwerty123@", "Тор");
        body = gson.toJson(newCourier);
        response = serviceSteps.sendPostRequest("/api/v1/courier", body);
        serviceSteps.checkRequestStatus(response, 201);
        LoginRequest loginRequest = new LoginRequest(newCourier.getLogin(), newCourier.getPassword());
        body = gson.toJson(loginRequest);
        response = serviceSteps.sendPostRequest("/api/v1/courier/login", body);
        serviceSteps.checkRequestStatus(response, 200);
        loginResponse = gson.fromJson(response.body().asString(), LoginCourierResponse.class);
    }

    @Test
    @DisplayName("Проверка успешного принятия заказа курьером")
    public void checkTakeOrder(){
        response = serviceSteps.sendPutRequestWithParam("/api/v1/orders/accept/"+createOrderResponse.getTrack(), "courierId", loginResponse.getId());
        serviceSteps.checkRequestStatus(response, 200);
        OkResponse okResponse = gson.fromJson(response.body().asString(), OkResponse.class);
        assertTrue(okResponse.isOk());
    }

    @Test
    @DisplayName("Проверка принятия заказа с несуществующим id курьера")
    public void checkTakeOrderWithWrongCourierNumber(){
        response = serviceSteps.sendPutRequestWithParam("/api/v1/orders/accept/"+createOrderResponse.getTrack(), "courierId", loginResponse.getId()+2);
        serviceSteps.checkRequestStatus(response, 404);
        ErrorResponse errorResponse = gson.fromJson(response.body().asString(), ErrorResponse.class);
        serviceSteps.checkRequestMessage("Курьера с таким id не существует", errorResponse.getMessage());
    }

    @Test
    @DisplayName("Проверка успешного принятия заказа с несуществующим номером заказа")
    public void checkTakeOrderWithWrongOrderNumber(){
        response = serviceSteps.sendPutRequestWithParam("/api/v1/orders/accept/"+createOrderResponse.getTrack()+2, "courierId", loginResponse.getId());
        ErrorResponse errorResponse = gson.fromJson(response.body().asString(), ErrorResponse.class);
        serviceSteps.checkRequestStatus(response, 404);
        serviceSteps.checkRequestMessage("Заказа с таким id не существует", errorResponse.getMessage());
    }

    @AfterEach
    public void tearDown() {
        String body = gson.toJson(createOrderResponse);
        serviceSteps.sendPutRequest("/api/v1/orders/cancel", body);
        serviceSteps.sendDeleteRequest("/api/v1/courier/"+loginResponse.getId());
    }
}
